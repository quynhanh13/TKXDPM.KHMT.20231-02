package subsystem;
import common.exception.PaymentException;
import entity.invoice.Invoice;
import entity.payment.PaymentTransaction;
import subsystem.paypal.PaypalSystemController;

import java.io.IOException;
import java.sql.SQLException;

/***
 * The {@code InterbankSubsystem} class is used to communicate with the
 * Interbank to make transaction.
 *
 * @author hieud
 *
 */
public class InterbankSubsystem implements InterbankInterface {

	private PaypalSystemController paypalSystemController;

	public InterbankSubsystem() {
		paypalSystemController = new PaypalSystemController();
	}

	public String getUrlPayOrder(int amount) {
		return paypalSystemController.getUrlPayOrder(amount);
	}


	public PaymentTransaction paypalPayOrder(Invoice invoice, String contents) throws IOException, SQLException {
		PaymentTransaction transaction = paypalSystemController.payOrder(invoice,contents);
		return transaction;
	}

	public void refundOrder(Invoice invoice) throws IOException, SQLException, PaymentException {
		paypalSystemController.refundOrder(invoice);
	}
}
