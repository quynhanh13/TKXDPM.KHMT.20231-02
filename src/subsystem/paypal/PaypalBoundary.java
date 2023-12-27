package subsystem.paypal;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.util.Base64;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;


public class PaypalBoundary {

    private String clientId = "AWLmNpASD_gPRrOncXOFnrp1qhX8dSqGfqQrTVrLJu2dTu75zkuXYmInv__TyVuqLT1Zkmfud7cFnCSk";
    private String secret = "EO4VV5_sJIQJlQa5GFQf63u-qc7BoU9V5HupcDu1n7mvmZLY6vD9-Pb3xElKLhcFFG7i94h9UhBMWEOf";

    public String createOrder(int amount){
        String paypalApiUrl = "https://api.sandbox.paypal.com/v2/checkout/orders";

        HttpClient httpClient = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost(paypalApiUrl);

        // Set headers
        httpPost.setHeader("Content-Type", "application/json");
        httpPost.setHeader("Authorization", "Basic " + Base64.getEncoder().encodeToString((clientId + ":" + secret).getBytes()));

        // Set request body (example request body)
        String requestBody = String.format("{\n" +
                "  \"intent\": \"CAPTURE\",\n" +
                "  \"purchase_units\": [\n" +
                "    {\n" +
                "      \"amount\": {\n" +
                "        \"currency_code\": \"USD\",\n" +
                "        \"value\": \"%.2f\"\n" +
                "      }\n" +
                "    }\n" +
                "  ]\n" +
                "}", (double) amount);
        httpPost.setEntity(new StringEntity(requestBody, "UTF-8"));

        try {
            // Execute the request
            HttpResponse response = httpClient.execute(httpPost);
            HttpEntity entity = response.getEntity();

            if (entity != null) {
                // Parse the response and extract the approval URL
                String jsonResponse = EntityUtils.toString(entity);
                return jsonResponse;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public HttpResponse capturePayOrder(String id) throws IOException {
        String paypalApiUrl = "https://api.sandbox.paypal.com/v2/checkout/orders/"+ id +"/capture";

        HttpClient httpClient = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost(paypalApiUrl);

        String accessToken = getAccessToken(clientId,secret);
        System.out.println(accessToken);
        // Set headers
        httpPost.setHeader("Content-Type", "application/json");
        httpPost.setHeader("Authorization", "Bearer " + accessToken);

        try {
            // Execute the request
            HttpResponse response = httpClient.execute(httpPost);
            return response;
        } catch (IOException e) {
            throw e;
        }
    }

    public HttpResponse refundPayOrder(String id) throws IOException {
        String paypalApiUrl = "https://api.sandbox.paypal.com/v2/payments/captures/" + id + "/refund";

        HttpClient httpClient = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost(paypalApiUrl);

        String accessToken = getAccessToken(clientId,secret);
        // Set headers
        httpPost.setHeader("Content-Type", "application/json");
        httpPost.setHeader("Authorization", "Bearer " + accessToken);

        try {
            // Execute the request
            HttpResponse response = httpClient.execute(httpPost);
            return response;
        } catch (IOException e) {
            throw e;
        }
    }

    private static String getAccessToken(String clientId, String secret) {
        String paypalOAuthUrl = "https://api.sandbox.paypal.com/v1/oauth2/token";

        HttpClient httpClient = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost(paypalOAuthUrl);

        // Set headers
        httpPost.setHeader("Content-Type", "application/x-www-form-urlencoded");
        httpPost.setHeader("Authorization", "Basic " + Base64.getEncoder().encodeToString((clientId + ":" + secret).getBytes()));

        // Set request body
        String requestBody = "grant_type=client_credentials";
        httpPost.setEntity(new StringEntity(requestBody, "UTF-8"));

        try {
            // Execute the request
            HttpResponse response = httpClient.execute(httpPost);
            HttpEntity entity = response.getEntity();

            if (entity != null) {
                // Parse the response and extract the access token
                String jsonResponse = EntityUtils.toString(entity);
                JsonObject jsonObject = JsonParser.parseString(jsonResponse).getAsJsonObject();
                return jsonObject.get("access_token").getAsString();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
}
