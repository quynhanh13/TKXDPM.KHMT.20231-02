package views.screen.invoice;

import entity.invoice.Invoice;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import views.screen.BaseScreenHandler;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;

public class InvoiceDetailHandler extends BaseScreenHandler {

    private Invoice invoice;

    @FXML
    private Label id;

    @FXML
    private Label orderId;

    @FXML
    private Label amount;

    @FXML
    private Label paypalId;

    @FXML
    private Label status;

    @FXML
    private ImageView aimsImage;

    public InvoiceDetailHandler(Stage stage, Invoice invoice, String screenPath) throws IOException, SQLException {
        super(stage, screenPath);

        this.invoice = invoice;

        File file = new File("assets/images/Logo.png");
        Image im = new Image(file.toURI().toString());
        aimsImage.setImage(im);

        aimsImage.setOnMouseClicked(e -> {
            homeScreenHandler.show();
        });

        id.setText(String.valueOf(invoice.getId()));
//        orderId.setText(String.valueOf(invoice.getOrder().getID()));
        amount.setText(String.valueOf(invoice.getAmount()));
        paypalId.setText(invoice.getPaypalId());
        status.setText(invoice.getStatus());
    }

    public void requestToDetail(BaseScreenHandler prevScreen) throws SQLException {
        setPreviousScreen(prevScreen);
        setScreenTitle("Invoice Detail Screen");
        show();
    }
}
