package xyzCoin;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

import static junit.framework.TestCase.assertEquals;

/**
 * Created by Tom on 14/12/2017.
 *
 * Tests server implementation
 */

public class FeatureTest {
  private Server server;

  @Before
  public void initialization(){
    server = new Server(4);
    System.out.print("Started Server");
  }

  @After
  public void tearDown(){
    server.stop();
    System.out.print("Closed Server");
  }

  @Test
  public void postWalletsShouldCreateANewWallet() throws Exception {
    System.out.print("Ran Test");
    ResponseObject response = makeWallet("test01", "password");
    assertEquals(201, response.getStatus());
  }

  @Test
  public void postDuplicateWalletsShouldThrowException() throws Exception {
    ResponseObject response1 = makeWallet("test01", "password");
    assertEquals(201, response1.getStatus());
    ResponseObject response2 = makeWallet("test01", "password");
    assertEquals(409, response2.getStatus());
  }

  private ResponseObject makeWallet(String name, String password) throws IOException {
    JSONObject data = new JSONObject();
    data.put("name", name);
    data.put("password", password);
    return makePostRequest("http://localhost:4567/wallets", data.toJSONString());
  }


  private ResponseObject makePostRequest(String url, String data) throws IOException {
    CloseableHttpClient httpclient = HttpClients.createDefault();
    StringEntity requestEntity = new StringEntity(data, ContentType.APPLICATION_JSON);
    HttpPost postMethod = new HttpPost(url);
    postMethod.setEntity(requestEntity);
    HttpResponse response = httpclient.execute(postMethod);
    int status = response.getStatusLine().getStatusCode();
    HttpEntity entity = response.getEntity();
    String body = entity != null ? EntityUtils.toString(entity) : null;
    try {
      return new ResponseObject(status, body);
    } catch (ParseException e) {
      return null;
    }
  }

  private class ResponseObject{
    int status;
    JSONObject body;

    ResponseObject(int status, String body) throws ParseException {
      this.status = status;
      JSONParser parser = new JSONParser();
      Object obj = parser.parse(body);
      this.body = (JSONObject)obj;
    }
    int getStatus() {
      return status;
    }
    public JSONObject getBody() {
      return body;
    }
  }

}
