package controller;

import java.util.Calendar;
import java.util.Hashtable;
import java.util.Map;

import common.exception.InvalidCardException;
import common.exception.PaymentException;
import common.exception.UnrecognizedException;
import entity.cart.Cart;
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
//funtional cohesion
public class PaymentController extends BaseController {

	/**
	 * Represent the card used for payment
	 */
	private CreditCard card;

	/**
	 * Represent the Interbank subsystem
	 */
	private InterbankInterface interbank;


	public PaymentController(InterbankInterface interbank) {
		this.interbank = interbank;
	}


	/**
	 * Pay order, and then return the result with a message.
	 *
	 * @param amount         - the amount to pay
	 * @param contents       - the transaction contents
	 * @return {@link java.util.Map Map} represent the payment result with a
	 *         message.
	 */
	public Map<String, String> payOrder(int amount, String contents) {
		Map<String, String> result = new Hashtable<String, String>();
		result.put("RESULT", "PAYMENT FAILED!");
		try {
			/* this.interbank = new InterbankSubsystem();
			* Ở đây, nếu khởi tạo trực tiếp InterbankSubsystem() trong PaymentController làm cho
			* PaymentController phụ thuộc chặt chẽ vào một implementaion cụ thể của InterbankInterface
			* Như vậy, vi phạm nguyên tắc Dependency Inversion Principle (D)
			* Cách giải quyết : Chuyển InterbankSubsystem thành một tham số trong constructor
			* */
			PaymentTransaction transaction = interbank.paypalPayOrder(amount,contents);
			result.put("RESULT", "PAYMENT SUCCESSFUL!");
			result.put("MESSAGE", "You have succesffully paid the order!");
		} catch (PaymentException | UnrecognizedException ex) {
			result.put("MESSAGE", ex.getMessage());
		}
		return result;
	}

	/*
	public void emptyCart(){
		Cart.getCart().emptyCart();
	}
	* Phương thức emptyCart nằm trong PaymentContronller
	* Như vậy Payment Controller vừa phải thực hiện chức năng thanh toán
	* vừa phải thực hiện chức năng làm rỗng cart ( vi phạm Single Responsibility Principle )
	* Tách emptyCart ra lớp khác
	* */
}