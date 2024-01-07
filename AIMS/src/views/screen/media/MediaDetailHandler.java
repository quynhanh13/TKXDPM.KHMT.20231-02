package views.screen.media;

import entity.media.Media;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import views.screen.BaseScreenHandler;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;

public class MediaDetailHandler extends BaseScreenHandler {

    private Media media;

    @FXML
    private Label title;

    @FXML
    private Label type;

    @FXML
    private Label category;

    @FXML
    private Label price;

    @FXML
    private Label quantity;

    @FXML
    private ImageView image_detail;

    @FXML
    private ImageView aimsImage;

    public MediaDetailHandler(Stage stage, Media media, String screenPath) throws IOException, SQLException {
        super(stage, screenPath);

        this.media = media;

        File file = new File("assets/images/Logo.png");
        Image im = new Image(file.toURI().toString());
        aimsImage.setImage(im);

        file = new File(media.getImageURL());
        im = new Image(file.toURI().toString());
        image_detail.setImage(im);

        aimsImage.setOnMouseClicked(e -> {
            homeScreenHandler.show();
        });

        type.setText(media.getType());
        title.setText(media.getTitle());
        category.setText(media.getCategory());
        price.setText(String.valueOf(media.getPrice()));
        quantity.setText(String.valueOf(media.getQuantity()));
    }

    public void requestToDetail(BaseScreenHandler prevScreen) throws SQLException {
        setPreviousScreen(prevScreen);
        setScreenTitle("Media Detail Screen");
        show();
    }
}
