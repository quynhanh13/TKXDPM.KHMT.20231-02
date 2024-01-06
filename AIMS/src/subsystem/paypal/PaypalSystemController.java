package subsystem.paypal;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import common.exception.PaymentException;
import entity.invoice.Invoice;
import entity.payment.PaymentTransaction;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;

import utils.Utils;

import java.io.IOException;
import java.sql.SQLException;

public class PaypalSystemController {

    private static final String PAY_COMMAND = "pay";
    private static final String VERSION = "1.0.0";
    private PaypalBoundary paypalBoundary = new PaypalBoundary();

    public PaymentTransaction payOrder(Invoice invoice, String contents) throws IOException, SQLException {

        try {
            //Data Coupling
            HttpResponse response = paypalBoundary.capturePayOrder(invoice.getPaypalId());
            String statusCode = String.valueOf(response.getStatusLine().getStatusCode());
            statusCode = HandleException.handleStatusCodePayment(statusCode);
            invoice.updateStatus("PAYMENT COMPLETED");
            PaymentTransaction trans = new PaymentTransaction(invoice.getId(), Utils.getToday().toString(), extractRefundLink(EntityUtils.toString(response.getEntity())));
            trans.saveTransaction();
            return trans;
        }catch (Exception e){
            e.printStackTrace();
            throw e;
        }
    }

    public void refundOrder(Invoice invoice) throws IOException, SQLException {
        try {
            HttpResponse response = paypalBoundary.refundPayOrder(PaymentTransaction.getRefundId(invoice.getId()));
            String statusCode = String.valueOf(response.getStatusLine().getStatusCode());
            statusCode = HandleException.handleStatusCodeRefund(statusCode);
            invoice.updateStatus("REFUND");
        }
        catch (PaymentException e){
            throw e;
        }
    }

    public String getUrlPayOrder(int amount){
        String jsonString = paypalBoundary.createOrder(amount);

        if(jsonString == null)return null;

        JsonObject jsonObject = JsonParser.parseString(jsonString).getAsJsonObject();

        // Lấy giá trị của "id"
        String id = jsonObject.get("id").getAsString();
        return id;
    }


    private String extractRefundLink(String jsonResponse) {
        JsonObject jsonOrder = JsonParser.parseString(jsonResponse).getAsJsonObject();

        // Get the "purchase_units" array
        JsonArray purchaseUnits = jsonOrder.getAsJsonArray("purchase_units");

        // Kiểm tra xem purchase_units có phần tử không
        if (purchaseUnits.size() > 0) {
            // Get the first element of the array
            JsonObject purchaseUnit = purchaseUnits.get(0).getAsJsonObject();

            // Get the "payments" object
            JsonObject payments = purchaseUnit.getAsJsonObject("payments");

            // Kiểm tra xem payments có tồn tại không
            if (payments != null) {
                // Get the "captures" array
                JsonArray captures = payments.getAsJsonArray("captures");

                // Kiểm tra xem captures có phần tử không
                if (captures.size() > 0) {
                    // Get the first element of the captures array
                    JsonObject capture = captures.get(0).getAsJsonObject();

                    // Get the "links" array from the capture
                    JsonArray links = capture.getAsJsonArray("links");

                    // Iterate through the links array to find the "refund" link
                    for (int i = 0; i < links.size(); i++) {
                        JsonObject link = links.get(i).getAsJsonObject();
                        // Kiểm tra xem "rel" và "href" có tồn tại không
                        if (link.has("rel") && link.has("href")) {
                            String rel = link.get("rel").getAsString();

                            // Check if the link is the refund link
                            if ("refund".equals(rel)) {
                                // Get the href (link) value
                                String refundLink = link.get("href").getAsString();
                                // Extract the capture ID from the refund link
                                int capturesIndex = refundLink.indexOf("captures/");
                                String tmp = refundLink.substring(capturesIndex + "captures/".length());
                                return tmp.replaceAll("/refund", "");
                            }
                        }
                    }
                }
            }
        }

        return null;
    }
}
