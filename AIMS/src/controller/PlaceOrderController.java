package controller;

import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.logging.Logger;

import entity.cart.Cart;
import entity.cart.CartMedia;
import common.exception.InvalidDeliveryInfoException;
import entity.invoice.Invoice;
import entity.media.Media;
import entity.order.Order;
import entity.order.OrderMedia;
import subsystem.InterbankInterface;
import subsystem.InterbankSubsystem;
import views.screen.popup.PopupScreen;

/**
 * This class controls the flow of place order usecase in our AIMS project
 * @author nguyenlm
 */
public class PlaceOrderController extends BaseController{

    private InterbankInterface interbankInterface;
    /**
     * Just for logging purpose
     */
    private static Logger LOGGER = utils.Utils.getLogger(PlaceOrderController.class.getName());

    /**
     * This method checks the avalibility of product when user click PlaceOrder button
     * @throws SQLException
     */
    public void placeOrder() throws SQLException{
        Cart.getCart().checkAvailabilityOfProduct();
    }

    /**
     * This method creates the new Order based on the Cart
     * @return Order
     * @throws SQLException
     */
    public Order createOrder() throws SQLException{
        Order order = new Order();
        for (Object object : Cart.getCart().getListMedia()) {
            CartMedia cartMedia = (CartMedia) object;
            OrderMedia orderMedia = new OrderMedia(cartMedia.getMedia(),
                    cartMedia.getQuantity(),
                    cartMedia.getPrice());
            order.addOrderMedia(orderMedia);
        }
        return order;
    }

    /**
     * This method creates the new Invoice based on order
     * @param order
     * @return Invoice
     */
    public Invoice createInvoice(Order order) throws SQLException {
        this.interbankInterface = new InterbankSubsystem();
        String id = this.interbankInterface.getUrlPayOrder(order.getAmount() + calculateShippingFee(order));
        Invoice invoice = new Invoice(order, id);
        invoice.saveInvoice();
        return invoice;
    }

    /**
     * This method takes responsibility for processing the shipping info from user
     * @param info
     * @throws InterruptedException
     * @throws IOException
     */
    public void processDeliveryInfo(HashMap info) throws InterruptedException, IOException{
        LOGGER.info("Process Delivery Info");
        LOGGER.info(info.toString());
        validateDeliveryInfo(info);
    }

    /**
     * The method validates the info
     * @param info
     * @throws InterruptedException
     * @throws IOException
     */
    public void validateDeliveryInfo(HashMap<String, String> info) throws InterruptedException, IOException{
        if(this.validateName(info.get("name"))){
            throw  new InvalidDeliveryInfoException("Invalid name");
        }
        if(this.validatePhoneNumber(info.get("phone"))){
            throw  new InvalidDeliveryInfoException("Invalid phone number");
        }
        if(this.validateAddress(info.get("address"))){
            throw  new InvalidDeliveryInfoException("Invalid address");
        }
    }

    public boolean validatePhoneNumber(String phoneNumber) {
        if(phoneNumber == null || phoneNumber.trim().isEmpty()) return true;
        return !phoneNumber.matches("\\d{10}");
    }

    public boolean validateName(String name) {
        if(name == null || name.trim().isEmpty()) return true;
        return !name.matches("^[a-zA-Z ]*$");
    }

    public boolean validateAddress(String address) {
        if(address == null || address.trim().isEmpty()) return true;
        return !address.matches("^[a-zA-Z ]*$");
    }


    /**
     * This method calculates the shipping fees of order
     * @param order
     * @return shippingFee
     */
    public int calculateShippingFee(Order order){
        HashMap<String, String> deliveryInfo = order.getDeliveryInfo();
        String province = "";
        if(deliveryInfo.get("province") != null) province = deliveryInfo.get("province");
        int fees = 0;

        if(order.getAmount() < 100) {
            double maxWeigh = maxWeigh(order);
            switch (province) {
                case "Hồ Chí Minh":
                case "Hà Nội":
                    if(maxWeigh <= 3.0){
                        fees = 22;
                    }
                    else {
                        fees = (int) (22 + (maxWeigh - 3.0)*5);
                    }
                    break;
                default:
                    if(maxWeigh <= 0.5){
                        fees = 30;
                    }
                    else {
                        fees = (int) (30 + (maxWeigh - 0.5)*5);
                    }
                    break;
            }
        }
        LOGGER.info("Order Amount: " + order.getAmount() + " -- Shipping Fees: " + fees);
        return fees;
    }

    private double maxWeigh(Order order){
        double max = 0;
        for(Object object: order.getlstOrderMedia()){
            OrderMedia orderMedia = (OrderMedia) object;
            Media media = (Media) orderMedia.getMedia();
            max = Math.max(max, media.getWeigh());
        }
        return max;
    }
}
