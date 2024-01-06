package controller;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.Hashtable;
import java.util.Map;

import common.exception.InvalidCardException;
import common.exception.PaymentException;
import common.exception.UnrecognizedException;
import entity.cart.Cart;
import entity.invoice.Invoice;
import entity.payment.CreditCard;
import entity.payment.PaymentTransaction;
import subsystem.InterbankInterface;
import subsystem.InterbankSubsystem;


/**
 * This {@code PaymentController} class control the flow of the payment process
 * in our AIMS Software.
 *
 * @author hieud
 *
 */
public class PaymentController extends BaseController {

	/**
	 * Represent the Interbank subsystem
	 */
	private InterbankInterface interbank;

	public PaymentController(InterbankInterface interbank){
		this.interbank = interbank;
	}

	public Map<String, String> payOrder(Invoice invoice, String contents) {
		Map<String, String> result = new Hashtable<String, String>();
		result.put("RESULT", "PAYMENT FAILED!");
		try {
			PaymentTransaction transaction = interbank.paypalPayOrder(invoice,contents);

			result.put("RESULT", "PAYMENT SUCCESSFUL!");
			result.put("MESSAGE", "You have succesffully paid the order!");
		} catch (PaymentException | UnrecognizedException | IOException ex) {
			result.put("MESSAGE", ex.getMessage());
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
		return result;
	}

	public void refundOrder(Invoice invoice) throws IOException, SQLException {
		try {
			interbank.refundOrder(invoice);
		}catch (PaymentException e){
			throw e;
		}
	}

	public void emptyCart(){
		//Content Coupling
		Cart.getCart().emptyCart();
	}
}