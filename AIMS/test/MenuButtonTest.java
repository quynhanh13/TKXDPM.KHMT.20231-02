import entity.media.Media;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import views.screen.home.HomeScreenHandler;
import views.screen.home.MediaHandler;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class MenuButtonTest {

    private HomeScreenHandler homeScreenHandler;
    private List<MediaHandler> testItems;
    private HBox hboxMedia;

    @BeforeEach
    public void setUp() throws SQLException {
        // Khởi tạo các mock và đối tượng cần thiết
        homeScreenHandler = mock(HomeScreenHandler.class);
        hboxMedia = mock(HBox.class);

        // Khởi tạo danh sách homeItems (các MediaHandler)
        testItems = new ArrayList<>();

        Media testMedia1 = new Media(1, "Title 1", "Category 1", 45, 5, "Type 1");
        MediaHandler m1 = mock(MediaHandler.class);
        when(m1.getMedia()).thenReturn(testMedia1);

        Media testMedia2 = new Media(2, "Another ", "Category 2", 150, 3, "Type 2");
        MediaHandler m2 = mock(MediaHandler.class);
        when(m2.getMedia()).thenReturn(testMedia2);

        Media testMedia3 = new Media(3, "Title 3", "Category 1", 70, 7, "Type 1");
        MediaHandler m3 = mock(MediaHandler.class);
        when(m3.getMedia()).thenReturn(testMedia3);

        // Thêm các MediaHandler vào danh sách testItems
        testItems.add(m1);
        testItems.add(m2);
        testItems.add(m3);
    }

    @Test
    public void testFilterMediaByMenuButton0() {
        String keyword = "title";
        List<MediaHandler> filteredItems = FilterHelper.filterMediaByMenuButton(keyword, testItems);
        assertEquals(2, filteredItems.size());
    }
    @Test
    public void testFilterMediaByMenuButton1() {
        String keyword = "<20đ";
        List<MediaHandler> filteredItems = FilterHelper.filterMediaByMenuButton(keyword, testItems);
        assertEquals(0, filteredItems.size());
    }

    @Test
    public void testFilterMediaByMenuButton2() {
        String keyword = "20đ-50đ";
        List<MediaHandler> filteredItems = FilterHelper.filterMediaByMenuButton(keyword, testItems);
        assertEquals(1, filteredItems.size());
    }

    @Test
    public void testFilterMediaByMenuButton3() {
        String keyword = "50đ-100đ";
        List<MediaHandler> filteredItems = FilterHelper.filterMediaByMenuButton(keyword, testItems);
        assertEquals(1, filteredItems.size());
    }
    @Test
    public void testFilterMediaByMenuButton4() {
        String keyword = ">100đ";
        List<MediaHandler> filteredItems = FilterHelper.filterMediaByMenuButton(keyword, testItems);
        assertEquals(1, filteredItems.size());
    }


}
