package views.screen.home;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.*;
import java.util.logging.Logger;

import common.exception.ViewCartException;
import controller.HomeController;
import controller.InvoiceListController;
import controller.ViewCartController;
import entity.cart.Cart;
import entity.invoice.Invoice;
import entity.media.Media;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import utils.Configs;
import utils.Utils;
import views.screen.BaseScreenHandler;
import views.screen.cart.CartScreenHandler;
import views.screen.invoice.InvoiceDetailHandler;
import views.screen.invoicelist.InvoiceListHandler;
import views.screen.media.MediaDetailHandler;
import views.screen.popup.PopupScreen;


public class HomeScreenHandler extends BaseScreenHandler implements Initializable{

    public static Logger LOGGER = Utils.getLogger(HomeScreenHandler.class.getName());

    @FXML
    private Label numMediaInCart;

    @FXML
    private ImageView aimsImage;

    @FXML
    private Label currentPageLabel;

    @FXML
    private ImageView cartImage;

    @FXML
    private VBox vboxMedia1;

    @FXML
    private VBox vboxMedia2;

    @FXML
    private VBox vboxMedia3;

    @FXML
    private HBox hboxMedia;

    @FXML
    private TextField searchField;

    @FXML
    private SplitMenuButton splitMenuBtnSearch;


    @FXML
    private ImageView invoiceList;

    private List homeItems;

    private List displayedItems;

    public static HomeScreenHandler _instance;

    public HomeScreenHandler(Stage stage, String screenPath) throws IOException{
        super(stage, screenPath);
    }

    public Label getNumMediaCartLabel(){
        return this.numMediaInCart;
    }

    public HomeController getBController() {
        return (HomeController) super.getBController();
    }

    @Override
    public void show() {
        numMediaInCart.setText(String.valueOf(Cart.getCart().getListMedia().size()) + " media");
        super.show();
    }
    private int currentPage = 0;
    private final int itemsPerPage = 12;

    @FXML
    private void showNextMedia(MouseEvent event) {
        int startIndex = currentPage * itemsPerPage;
        int endIndex = Math.min(startIndex + itemsPerPage, displayedItems.size());

        if (endIndex < displayedItems.size()) {
            currentPage++;
            List<MediaHandler> displayedItems = updateMediaDisplay(this.displayedItems);
            addMediaHome(displayedItems);
        }
    }

    @FXML
    private void showPreviousMedia(MouseEvent event) {
        if (currentPage > 0) {
            currentPage--;
            List<MediaHandler> displayedItems = updateMediaDisplay(this.displayedItems);
            addMediaHome(displayedItems);
        }
    }

    private List<MediaHandler> updateMediaDisplay( List Items) {
        int startIndex = currentPage * itemsPerPage;
        int endIndex = Math.min(startIndex + itemsPerPage, Items.size());
        List<MediaHandler> displayedItems = new ArrayList<>(Items.subList(startIndex, endIndex));

        int totalPages = (int) Math.ceil((double) Items.size() / itemsPerPage);
        int currentDisplayPage = currentPage + 1;
        currentPageLabel.setText("Page " + currentDisplayPage + " of " + totalPages);
        return displayedItems;
    }

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        setBController(new HomeController());
        try{
            List medium = getBController().getAllMedia();
            this.homeItems = new ArrayList<>();
            for (Object object : medium) {
                Media media = (Media)object;
                MediaHandler m1 = new MediaHandler(Configs.HOME_MEDIA_PATH, media, this);
                this.homeItems.add(m1);
            }
            this.displayedItems = this.homeItems;
        }catch (SQLException | IOException e){
            LOGGER.info("Errors occured: " + e.getMessage());
            e.printStackTrace();
        }


        aimsImage.setOnMouseClicked(e -> {
            List<MediaHandler> displayedItems = updateMediaDisplay(this.homeItems);
            addMediaHome(displayedItems);
        });

        cartImage.setOnMouseClicked(e -> {
            CartScreenHandler cartScreen;
            try {
                LOGGER.info("User clicked to view cart");
                cartScreen = new CartScreenHandler(this.stage, Configs.CART_SCREEN_PATH);
                cartScreen.setHomeScreenHandler(this);
                cartScreen.setBController(new ViewCartController());
                cartScreen.requestToViewCart(this);
            } catch (IOException | SQLException e1) {
                throw new ViewCartException(Arrays.toString(e1.getStackTrace()).replaceAll(", ", "\n"));
            }
        });

        invoiceList.setOnMouseClicked(e -> {
            InvoiceListHandler invoiceListHandler;
            try {
                invoiceListHandler = new InvoiceListHandler(this.stage, Configs.INVOICE_LIST_PATH);
                invoiceListHandler.setHomeScreenHandler(this);
                invoiceListHandler.setBController(new InvoiceListController());
                invoiceListHandler.requestToInvoiceList(this);
            } catch (IOException | SQLException ex) {
                throw new RuntimeException(ex);
            }
        });

        addMediaHome(this.homeItems);
        addMenuItem(0, "Book", splitMenuBtnSearch);
        addMenuItem(1, "DVD", splitMenuBtnSearch);
        addMenuItem(2, "CD", splitMenuBtnSearch);
        addMenuItem(3, "<20đ", splitMenuBtnSearch);
        addMenuItem(4, "20đ-50đ", splitMenuBtnSearch);
        addMenuItem(5, "50đ-100đ", splitMenuBtnSearch);
        addMenuItem(6, ">100đ", splitMenuBtnSearch);
    }

    public void setImage(){
        // fix image path caused by fxml
        File file1 = new File(Configs.IMAGE_PATH + "/" + "Logo.png");
        Image img1 = new Image(file1.toURI().toString());
        aimsImage.setImage(img1);

        File file2 = new File(Configs.IMAGE_PATH + "/" + "cart.png");
        Image img2 = new Image(file2.toURI().toString());
        cartImage.setImage(img2);

        File file3 = new File(Configs.IMAGE_PATH + "/" + "invoice.png");
        Image img3 = new Image(file3.toURI().toString());
        invoiceList.setImage(img3);
    }

    public void addMediaHome(List items){
        ArrayList mediaItems = (ArrayList)((ArrayList) items).clone();
        hboxMedia.getChildren().forEach(node -> {
            VBox vBox = (VBox) node;
            vBox.getChildren().clear();
        });
        while(!mediaItems.isEmpty()){
            hboxMedia.getChildren().forEach(node -> {
                int vid = hboxMedia.getChildren().indexOf(node);
                VBox vBox = (VBox) node;
                while(vBox.getChildren().size()<3 && !mediaItems.isEmpty()){
                    MediaHandler media = (MediaHandler) mediaItems.get(0);
                    vBox.getChildren().add(media.getContent());
                    mediaItems.remove(media);
                }
            });
            return;
        }
    }

    public void addMenuItem(int position, String text, MenuButton menuButton){
        MenuItem menuItem = new MenuItem();
        Label label = new Label();
        label.prefWidthProperty().bind(menuButton.widthProperty().subtract(31));
        label.setText(text);
        label.setTextAlignment(TextAlignment.RIGHT);
        menuItem.setGraphic(label);
        menuItem.setOnAction(e -> {
            // empty home media
            hboxMedia.getChildren().forEach(node -> {
                VBox vBox = (VBox) node;
                vBox.getChildren().clear();
            });

            // filter only media with the choosen category
            List<MediaHandler> filteredItems = new ArrayList<>();
            homeItems.forEach(me -> {
                MediaHandler media = (MediaHandler) me;
                if (media.getMedia().getTitle().toLowerCase().startsWith(text.toLowerCase())){
                    filteredItems.add(media);
                }else{
                    if (text.equals("<20đ")) {
                        if (media.getMedia().getPrice() < 20) {
                            filteredItems.add(media);
                        }

                    } else if (text.equals("20đ-50đ")) {
                        if (media.getMedia().getPrice() >= 20 && media.getMedia().getPrice() < 50) {
                            filteredItems.add(media);
                        }
                    } else if (text.equals("50đ-100đ")) {
                        if (media.getMedia().getPrice() >= 50 && media.getMedia().getPrice() <= 100) {
                            filteredItems.add(media);
                        }
                    }
                    else if (text.equals(">100đ")) {
                        if (media.getMedia().getPrice() > 100) {
                            filteredItems.add(media);
                        }
                    }

                    Collections.sort(filteredItems, Comparator.comparingDouble(
                            mediax -> ((MediaHandler) mediax).getMedia().getPrice()));
                }

            });
            checkEmpty(filteredItems);
        });
        menuButton.getItems().add(position, menuItem);
    }

    @FXML
    void searchButtonClicked(MouseEvent event) throws SQLException, IOException {
        String searchText = searchField.getText().toLowerCase().trim();
        List<Media> medium = getBController().getAllMedia();
        List<Media> filteredMedia = getBController().filterMediaByKeyWord(searchText,medium);
//        List<MediaHandler> filteredItems = filterMediaByKeyWord(searchText, homeItems);
        List<MediaHandler> filteredItems = convertMediaHandlerList(filteredMedia);
        checkEmpty(filteredItems);
    }

    private void checkEmpty(List<MediaHandler> filteredItems) {
        if (filteredItems.isEmpty()) {
            try {
                PopupScreen.error("No matching products.");
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        } else {
            currentPage = 0;
            this.displayedItems = filteredItems;
            List<MediaHandler> displayedItems = updateMediaDisplay(filteredItems);
            addMediaHome(displayedItems);
        }
    }

    public List<MediaHandler> filterMediaByKeyWord(String keyword, List<MediaHandler> items) {
        List<MediaHandler> filteredItems = new ArrayList<>();
        for (Object item : items) {
            MediaHandler media = (MediaHandler) item;
            if (media.getMedia().getTitle().toLowerCase().contains(keyword)) {
                filteredItems.add(media);
            }
        }
        return filteredItems;
    }

    public void handleClickDetail(Media media){
        MediaDetailHandler mediaDetailHandler;
        try {
            mediaDetailHandler = new MediaDetailHandler(this.stage, media,Configs.MEDIA_DETAIL_PATH);
            mediaDetailHandler.requestToDetail(this);
            mediaDetailHandler.setHomeScreenHandler(this);
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
    }

    // public void handleInvoiceDetail(Invoice invoice){
    //     InvoiceDetailHandler invoiceDetailHandler;
    //     try {
    //         invoiceDetailHandler = new InvoiceDetailHandler(this.stage, invoice, Configs.INVOICE_DETAIL_PATH);
    //         invoiceDetailHandler.requestToDetail(this);
    //         invoiceDetailHandler.setHomeScreenHandler(this);
    //     } catch (IOException ex) {
    //         throw new RuntimeException(ex);
    //     } catch (SQLException ex) {
    //         throw new RuntimeException(ex);
    //     }
    // }

    public List<MediaHandler> convertMediaHandlerList(List<Media> items) throws SQLException, IOException {
        List<MediaHandler> mediaHandlerList = new ArrayList<>();
        for (Object item : items) {
            Media media = (Media) item ;
            MediaHandler m1 = new MediaHandler(Configs.HOME_MEDIA_PATH, media, this);
            mediaHandlerList.add(m1);
        }
        return mediaHandlerList;
    }
}

