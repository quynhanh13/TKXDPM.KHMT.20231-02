

import controller.PlaceRushOrderController;
import entity.media.Book;
import entity.media.Media;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.sql.SQLException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class CheckSupportedPlaceRushOrderTest {

    private PlaceRushOrderController placeRushOrderController;

    @BeforeEach
    public void setUp(){
        placeRushOrderController = new PlaceRushOrderController();
    }

    @Test
    public void testCheckSupportedPlaceRushOrder_True() throws SQLException {
        Book book = new Book();
        book.setIsSupportedPlaceRushOrder(true);
        boolean isValid = placeRushOrderController.checkSupportedPlaceRushOrder(book);
        Assert.assertTrue(isValid);
    }

    @Test
    public void testCheckSupportedPlaceRushOrder_False() throws SQLException {
        Book book = new Book();
        book.setIsSupportedPlaceRushOrder(false);
        boolean isValid = placeRushOrderController.checkSupportedPlaceRushOrder(book);
        Assert.assertEquals(isValid, false);
    }

}
