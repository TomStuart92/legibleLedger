package xyzCoinFeatures;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.junit.AfterClass;
import org.junit.Test;
import xyzCoin.InternalServerException;
import xyzCoin.Server;

import java.io.IOException;
import java.util.UUID;

import static junit.framework.TestCase.assertEquals;

/**
 * Created by Tom on 14/12/2017.
 *
 * Tests server implementation
 */

public class walletFeatureTest {
  private static Server server = new Server(4, null, null);

  @AfterClass
  public static void tearDown() throws InternalServerException {
    server.stop();
  }

  @Test
  public void postWalletsShouldCreateANewWallet() throws Exception {
    ResponseObject response = makeWallet("test01", "password");
    assertEquals(201, response.getStatus());
  }

  @Test
  public void postDuplicateWalletsShouldThrowException() throws Exception {
    ResponseObject response1 = makeWallet("test02", "password");
    assertEquals(201, response1.getStatus());
    ResponseObject response2 = makeWallet("test02", "password");
    assertEquals(409, response2.getStatus());
  }

  @Test
  public void getWalletShouldReturnBalance() throws Exception {
    String name = UUID.randomUUID().toString();
    makeWallet(name, "password");
    ResponseObject response = getWallet(name);
    assertEquals(200, response.getStatus());
    JSONObject responseBody = response.getBody();
    assertEquals(true, responseBody.containsKey("value"));
  }

  @Test
  public void postEmptyBodyShouldThrowException() throws Exception {
    ResponseObject response2 = makeWallet(null, null);
    assertEquals(400, response2.getStatus());
  }

  private ResponseObject makeWallet(String name, String password) throws IOException {
    JSONObject data = new JSONObject();
    data.put("name", name);
    data.put("password", password);
    return makePostRequest("http://localhost:4567/wallets", data.toJSONString());
  }

  private ResponseObject getWallet(String name) throws IOException {
    return makeGetRequest("http://localhost:4567/wallets/" + name);
  }

  private ResponseObject makeGetRequest(String url) throws IOException {
    CloseableHttpClient httpclient = HttpClients.createDefault();
    HttpGet getMethod = new HttpGet(url);
    HttpResponse response = httpclient.execute(getMethod);
    int status = response.getStatusLine().getStatusCode();
    HttpEntity entity = response.getEntity();
    String body = entity != null ? EntityUtils.toString(entity) : null;
    try {
      return new ResponseObject(status, body);
    } catch (ParseException e) {
      return null;
    }
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
