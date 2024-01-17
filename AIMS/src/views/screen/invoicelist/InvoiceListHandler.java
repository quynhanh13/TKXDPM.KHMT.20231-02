package views.screen.invoicelist;

import common.exception.PaymentException;
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
import views.screen.home.HomeScreenHandler;
import views.screen.invoice.InvoiceDetailHandler;
import views.screen.payment.PaymentScreenHandler;
import views.screen.popup.PopupScreen;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

import static views.screen.popup.PopupScreen.*;

public class InvoiceListHandler extends BaseScreenHandler {

    private ArrayList<Invoice> dataInvoice;

    private HomeScreenHandler home;

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

    @FXML
    private TableColumn<Invoice, Button> invoice_detail;

    public InvoiceListHandler(Stage stage, String screenPath) throws IOException, SQLException {
        super(stage, screenPath);

        loadData();

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

        invoice_detail.setCellValueFactory(cellData -> {
            Button detailButton = createDetailButton(cellData.getValue());
            return new SimpleObjectProperty<>(detailButton);
        });
        invoice_detail.setCellFactory(col -> new TableCell<>() {
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

    private Button createDetailButton(Invoice invoice) {
        Button detailButton = new Button();
        detailButton.setText("Detail");
//        String status = invoice.getStatus();

        detailButton.setOnMouseClicked(e -> {
            InvoiceDetailHandler invoiceDetailHandler;
            try {
                invoiceDetailHandler = new InvoiceDetailHandler(this.stage, invoice, Configs.INVOICE_DETAIL_PATH);
                invoiceDetailHandler.requestToDetail(this);
                invoiceDetailHandler.setHomeScreenHandler(HomeScreenHandler._instance);
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
        });

        return detailButton;
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
                    success("Refund successful", 3);
                    loadData();
                } catch (IOException | SQLException | PaymentException e) {
                    try {
                        error(e.getMessage());
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }
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

    private void loadData() throws SQLException {
        dataInvoice = Invoice.getListInvoice();
        initializeTableColumns();
        populateTable();
    }
}
