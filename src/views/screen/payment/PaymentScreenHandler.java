package views.screen.payment;

import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.util.Map;

import controller.PaymentController;
import entity.cart.Cart;
import common.exception.PlaceOrderException;
import entity.invoice.Invoice;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import utils.Configs;
import views.screen.BaseScreenHandler;
import views.screen.popup.PopupScreen;

public class PaymentScreenHandler extends BaseScreenHandler {

	@FXML
	private Button btnConfirmPayment;

	@FXML
	private ImageView loadingImage;

	@FXML
	private Label paymentLink;

	@FXML
	private Button btnGoToLink;

	private Invoice invoice;

	public PaymentScreenHandler(Stage stage, String screenPath, int amount, String contents) throws IOException {
		super(stage, screenPath);
	}

	// Data Coupling
	public PaymentScreenHandler(Stage stage, String screenPath, Invoice invoice) throws IOException {
		super(stage, screenPath);
		this.invoice = invoice;
		String url = "https://www.sandbox.paypal.com/checkoutnow?token=" + this.invoice.getPaypalId();
		paymentLink.setText(url);

		btnConfirmPayment.setOnMouseClicked(e -> {
			try {
				confirmToPayOrder();
				//Content Coupling
				((PaymentController) getBController()).emptyCart();
			} catch (Exception exp) {
				System.out.println(exp.getStackTrace());
			}
		});

		btnGoToLink.setOnMouseClicked(e -> {
			try {
				Desktop.getDesktop().browse(new URI(url));
			} catch (Exception exp) {
				System.out.println(exp.getStackTrace());
			}
		});
	}


	void confirmToPayOrder() throws IOException{
		String contents = "pay order";
		PaymentController ctrl = (PaymentController) getBController();
		Map<String, String> response = ctrl.payOrder(invoice, contents);

		BaseScreenHandler resultScreen = new ResultScreenHandler(this.stage, Configs.RESULT_SCREEN_PATH, response.get("RESULT"), response.get("MESSAGE") );
		resultScreen.setPreviousScreen(this);
		resultScreen.setHomeScreenHandler(homeScreenHandler);
		resultScreen.setScreenTitle("Result Screen");
		resultScreen.show();
	}

}