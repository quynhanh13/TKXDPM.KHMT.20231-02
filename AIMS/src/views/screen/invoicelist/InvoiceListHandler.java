package views.screen.invoicelist;

import controller.InvoiceListController;
import controller.PaymentController;
import entity.invoice.Invoice;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import subsystem.InterbankSubsystem;
import utils.Configs;
import views.screen.BaseScreenHandler;
import views.screen.payment.PaymentScreenHandler;
import views.screen.popup.PopupScreen;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

public class InvoiceListHandler extends BaseScreenHandler {

    private final ArrayList<Invoice> dataInvoice;

    @FXML
    private ImageView aimsImage;

    @FXML
    private TableView<Invoice> tableView;

    @FXML
    private TableColumn<Invoice, Integer> sttColumn;

    @FXML
    private TableColumn<Invoice, String> idColumn;

    @FXML
    private TableColumn<Invoice, Integer> amountColumn;

    @FXML
    private TableColumn<Invoice, String> statusColumn;

    @FXML
    private TableColumn<Invoice, Button> invoice_custom;

    public InvoiceListHandler(Stage stage, String screenPath) throws IOException, SQLException {
        super(stage, screenPath);

        dataInvoice = new Invoice().getListInvoice();
        initializeTableColumns();
        populateTable();

        File file = new File("assets/images/Logo.png");
        Image im = new Image(file.toURI().toString());
        aimsImage.setImage(im);

        // on mouse clicked, we back to home
        aimsImage.setOnMouseClicked(e -> {
            homeScreenHandler.show();
        });
    }

    private void initializeTableColumns() {
        // Map TableColumns to corresponding fields in Invoice class
        sttColumn.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getId()).asObject());
        idColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getPaypalId()));
        amountColumn.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getAmount()).asObject());
        statusColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getStatus()));

        invoice_custom.setCellValueFactory(cellData -> {
            Button customButton = createCustomButton(cellData.getValue());
            return new SimpleObjectProperty<>(customButton);
        });
        invoice_custom.setCellFactory(col -> new TableCell<>() {
            @Override
            protected void updateItem(Button button, boolean empty) {
                super.updateItem(button, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(button);
                }
            }
        });
    }

    private Button createCustomButton(Invoice invoice) {
        Button customButton = new Button();

        String status = invoice.getStatus();

        if ("CREATED".equals(status)) {
            customButton.setText("Payment");
            customButton.setOnAction(event -> {
                BaseScreenHandler paymentScreen = null;
                try {
                    paymentScreen = new PaymentScreenHandler(this.stage, Configs.PAYMENT_SCREEN_PATH, invoice);
                    InterbankSubsystem interbankSubsystem = new InterbankSubsystem();
                    paymentScreen.setBController(new PaymentController(interbankSubsystem));
                    paymentScreen.setPreviousScreen(this);
                    paymentScreen.setHomeScreenHandler(homeScreenHandler);
                    paymentScreen.setScreenTitle("Payment Screen");
                    paymentScreen.show();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });
        } else if ("PAYMENT COMPLETED".equals(status)) {
            customButton.setText("Refund");
            customButton.setOnAction(event -> {
                try {
                    InterbankSubsystem interbankSubsystem = new InterbankSubsystem();
                    PaymentController paymentController = new PaymentController(interbankSubsystem);
                    paymentController.refundOrder(invoice);
                    PopupScreen.success("Refund successful");
                } catch (IOException e) {
                    try {
                        PopupScreen.error(e.getMessage());
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            });
        }else {
            customButton.setText("Delete");
        }

        return customButton;
    }

    private void populateTable() {
        //Convert ArrayList to ObservableList and set it to the table
        ObservableList<Invoice> observableData = FXCollections.observableArrayList(dataInvoice);
        tableView.setItems(observableData);
    }

    public InvoiceListController getBController(){
        return (InvoiceListController) super.getBController();
    }

    public void requestToInvoiceList(BaseScreenHandler prevScreen) throws SQLException {
        setPreviousScreen(prevScreen);
        setScreenTitle("Invoice List Screen");
        show();
    }
}
