package xyzCoin;


import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import spark.Request;
import spark.Response;
import spark.Spark;

import static spark.Spark.awaitInitialization;
import static spark.Spark.get;
import static spark.Spark.post;

/**
 * Created by Tom on 12/10/2017.
 */

public class Server {
  private Controller controller;

  Server(int difficulty) {
    this.controller = new Controller(difficulty);
    controller.mineBlockchain();

    post("/wallets", this::createNewWallet);
    get("/wallets/:name", this::getWalletBalance);
    post("transactions", this::createNewTransaction);
    awaitInitialization();
  }

  void stop() {
    Spark.stop();
  }

  private String createNewWallet(Request req, Response res) {
    try {
      JSONObject body = parseRequest(req.body());
      String name = (String) body.get("name");
      String password = (String) body.get("password");
      if (name == null || password == null) throw new InvalidRequestException("Name or Password Null");
      controller.createNewWallet(name, password);
      res.status(201);
      return createResponseMessage("message", "success");
    } catch (InvalidRequestException | AlreadyExistsServerException | InternalServerException e) {
      res.status(e.getStatusCode());
      return createResponseMessage("error", e.getMessage());
    }
  }

  private String getWalletBalance(Request req, Response res) {
    String name = req.params(":name");
    Double balance = controller.getWalletBalance(name);
    return "{\"value\": \"" + balance + "\"}";
  }

  private String createNewTransaction(Request req, Response res) {
    try {
      JSONObject body = parseRequest(req.body());
      String from = (String) body.get("from");
      String password = (String) body.get("password");
      String to = (String) body.get("to");
      System.out.println(to);
      Double amount = (Double) body.get("amount");

      if (from == null || password == null || to == null || amount == null) throw new InvalidRequestException("Field Null");
      controller.sendCoin(password, from, amount, to);
      return createResponseMessage("message", "success");
    }  catch (InvalidRequestException | InternalServerException | InsufficientFundsException | ForbiddenServerException e) {
      res.status(e.getStatusCode());
      return createResponseMessage("error", e.getMessage());
    }
  }

  private static String createResponseMessage(String key, String message) {
    return "{\""+key+"\":\""+message+"\"}";
  }

  private static JSONObject parseRequest(String requestBody) throws InvalidRequestException {
    try {
      JSONParser parser = new JSONParser();
      Object obj = parser.parse(requestBody);
      return (JSONObject)obj;
    } catch (ParseException e) {
      throw new InvalidRequestException("Unable to parse body");
    }
  }


  public static void main(String[] args) {
    int difficulty = Integer.parseInt(args[0]);
    new Server(difficulty);
  }
}