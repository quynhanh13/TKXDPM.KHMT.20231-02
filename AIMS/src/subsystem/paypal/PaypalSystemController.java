package subsystem.paypal;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import common.exception.InvalidCardException;
import common.exception.UnrecognizedException;
import entity.payment.CreditCard;
import entity.payment.PaymentTransaction;
import org.apache.http.HttpResponse;
import utils.Configs;
import utils.MyMap;
import utils.Utils;

import java.util.Map;

public class PaypalSystemController {

    private static String id;

    private static final String PAY_COMMAND = "pay";
    private static final String VERSION = "1.0.0";
    private PaypalBoundary paypalBoundary = new PaypalBoundary();

    public PaymentTransaction payOrder(int amount, String contents) {
        Map<String, Object> transaction = new MyMap();
        transaction.put("command", PAY_COMMAND);
        transaction.put("transactionContent", contents);
        transaction.put("amount", amount);
        transaction.put("createdAt", Utils.getToday());

        Map<String, Object> requestMap = new MyMap();
        requestMap.put("version", VERSION);
        requestMap.put("transaction", transaction);

        try {
            //Content Coupling
            HttpResponse response = paypalBoundary.capturePayOrder(this.id);
            System.out.println(response.getEntity().toString());
            String statusCode = String.valueOf(response.getStatusLine().getStatusCode());
            statusCode = handleStatusCode(statusCode);
            PaymentTransaction trans = new PaymentTransaction(statusCode, contents, Integer.parseInt((String) transaction.get("amount")) , (String) transaction.get("createdAt"));
            return trans;

        }catch (Exception e){
            e.printStackTrace();
        }

        return null;
    }

    private String handleStatusCode(String statusCode){
        if(statusCode.equals("201")) return statusCode;
        else throw new UnrecognizedException();
    }

    public String getUrlPayOrder(int amount){
        String jsonString = paypalBoundary.createOrder(amount);

        if(jsonString == null)return null;

        JsonObject jsonObject = JsonParser.parseString(jsonString).getAsJsonObject();

        // Lấy giá trị của "id"
        String id = jsonObject.get("id").getAsString();

        this.id = id;

        return id;
    }
}
