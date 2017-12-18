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
import org.junit.Before;
import org.junit.Test;
import xyzCoin.Server;

import java.io.IOException;
import java.util.UUID;

import static junit.framework.TestCase.assertEquals;

/**
 * Created by Tom on 14/12/2017.
 *
 * Tests server implementation
 */

public class GetWalletFeatureTest {
  private Server server;
  private String name;

  @Before
  public void initialization() throws Exception {
    server = new Server(4);
    name = UUID.randomUUID().toString();
    makeWallet(name, "password");
  }
  
  @Test
  public void getWalletShouldReturnBalance() throws Exception {
    ResponseObject response = getWallet(name);
    assertEquals(200, response.getStatus());
    JSONObject responseBody = response.getBody();
    assertEquals(true, responseBody.containsKey("value"));
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
