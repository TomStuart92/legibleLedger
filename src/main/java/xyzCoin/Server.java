package xyzCoin;


import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import spark.Request;
import spark.Response;
import spark.Spark;

import static spark.Spark.get;
import static spark.Spark.post;

/**
 * Created by Tom on 12/10/2017.
 */

public class Server {
  private Controller controller;

  public Server(int difficulty, String blockchainStatePath, String walletStatePath) {
    this.controller = new Controller(difficulty, blockchainStatePath, walletStatePath);
    post("/wallets", this::createNewWallet);
    get("/wallets/:name", this::getWalletBalance);
    post("/transactions", this::createNewTransaction);
    this.controller.mineBlockchain();
  }

  public void stop() throws InternalServerException {
    Spark.stop();
    controller.shutdown();
  }

  private String createNewWallet(Request req, Response res) {
    try {
      JSONObject body = parseRequest(req.body());
      String name = (String) body.get("name");
      String password = (String) body.get("password");
      if (name == null || password == null) throw new InvalidRequestServerException("Name or Password Null");
      controller.createNewWallet(name, password);
      res.status(201);
      return createResponseMessage("message", "success");
    } catch (InvalidRequestServerException | AlreadyExistsServerException | InternalServerException e) {
      res.status(e.getStatusCode());
      return createResponseMessage("error", e.getMessage());
    }
  }

  private String getWalletBalance(Request req, Response res) throws NotFoundServerException {
    try {
      String name = req.params(":name");
      Double balance = controller.getWalletBalance(name);
      return "{\"value\": \"" + balance + "\"}";
    }  catch (NotFoundServerException e) {
      res.status(e.getStatusCode());
      return createResponseMessage("error", e.getMessage());
    }
  }

  private String createNewTransaction(Request req, Response res) {
    try {
      JSONObject body = parseRequest(req.body());
      String from = (String) body.get("from");
      String password = (String) body.get("password");
      String to = (String) body.get("to");
      Double amount = Double.parseDouble((String) body.get("amount"));

      if (from == null || password == null || to == null) throw new InvalidRequestServerException("Field Null");
      controller.sendCoin(password, from, amount, to);
      return createResponseMessage("message", "success");
    }  catch (InvalidRequestServerException | InternalServerException | InsufficientFundsServerException | ForbiddenServerException | NotFoundServerException e) {
      res.status(e.getStatusCode());
      return createResponseMessage("error", e.getMessage());
    }
  }

  private static String createResponseMessage(String key, String message) {
    return "{\""+key+"\":\""+message+"\"}";
  }

  private static JSONObject parseRequest(String requestBody) throws InvalidRequestServerException {
    try {
      JSONParser parser = new JSONParser();
      Object obj = parser.parse(requestBody);
      return (JSONObject)obj;
    } catch (ParseException e) {
      throw new InvalidRequestServerException("Unable to parse body");
    }
  }

  public static void main(String[] args) {
    int difficulty;
    String blockchainStatePath;
    String walletStatePath;
    if(args.length == 3) {
      difficulty = Integer.parseInt(args[0]);
      blockchainStatePath = args[1];
      walletStatePath = args[2];
    } else if (args.length == 1) {
      difficulty = Integer.parseInt(args[0]);
      blockchainStatePath = null;
      walletStatePath = null;
    } else {
      difficulty = 4;
      blockchainStatePath = "blockchainState.txt";
      walletStatePath = "walletControllerState.txt";
    }
    new Server(difficulty, blockchainStatePath, walletStatePath);
  }
}