package subsystem.paypal;

import common.exception.PaymentException;

public class HandleException {
    public static String handleStatusCodeRefund(String statusCode) throws PaymentException{
        if(statusCode.equals("201")) return statusCode;
        else {
            int errorCode = Integer.parseInt(statusCode);
            switch (errorCode){
                case 404:
                    throw new PaymentException("Order does not exist or expired");
                case 422:
                    throw new PaymentException("The capture has already been fully refunded");
                default:
                    throw new PaymentException("Refund fail");
            }
        }
    }

    public static String handleStatusCodePayment(String statusCode) throws PaymentException{
        if(statusCode.equals("201")) return statusCode;
        else {
            int errorCode = Integer.parseInt(statusCode);
            switch (errorCode){
                case 404:
                    throw new PaymentException("Order does not exist");
                default:
                    throw new PaymentException("Payment fail");
            }
        }
    }
}
