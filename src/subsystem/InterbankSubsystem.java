package subsystem;

import common.exception.InternalServerErrorException;
import common.exception.InvalidCardException;
import common.exception.NotEnoughBalanceException;
import entity.invoice.Invoice;
import entity.payment.CreditCard;
import entity.payment.PaymentTransaction;
import subsystem.interbank.InterbankSubsystemController;
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

	/**
	 * Represent the controller of the subsystem
	 */
	private InterbankSubsystemController ctrl;
	private PaypalSystemController paypalSystemController;

	/**
	 * Initializes a newly created {@code InterbankSubsystem} object so that it
	 * represents an Interbank subsystem.
	 */
	public InterbankSubsystem() {
		String str = new String();
		this.ctrl = new InterbankSubsystemController();
		paypalSystemController = new PaypalSystemController();
	}

	/**
	 * @see InterbankInterface#payOrder(entity.payment.CreditCard, int,
	 *      java.lang.String)
	 */
	public PaymentTransaction payOrder(CreditCard card, int amount, String contents) {
		PaymentTransaction transaction = ctrl.payOrder(card, amount, contents);
		return transaction;
	}

	/**
	 * @see InterbankInterface#refund(entity.payment.CreditCard, int,
	 *      java.lang.String)
	 */
	public PaymentTransaction refund(CreditCard card, int amount, String contents) {
		PaymentTransaction transaction = ctrl.refund(card, amount, contents);
		return transaction;
	}

	public String getUrlPayOrder(int amount) {
		return paypalSystemController.getUrlPayOrder(amount);
	}


	public PaymentTransaction paypalPayOrder(Invoice invoice, String contents) throws IOException, SQLException {
		PaymentTransaction transaction = paypalSystemController.payOrder(invoice,contents);
		return transaction;
	}

	public void refundOrder(Invoice invoice) throws IOException, SQLException {
		paypalSystemController.refundOrder(invoice);
	}
}
