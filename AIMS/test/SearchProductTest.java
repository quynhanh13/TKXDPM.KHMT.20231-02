import entity.media.Media;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import utils.Configs;
import views.screen.home.HomeScreenHandler;
import views.screen.home.MediaHandler;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;


public class SearchProductTest {

    private HomeScreenHandler homeScreenHandler;
    private List<MediaHandler> testItems = new ArrayList<>();

    @BeforeEach
    public void setUp() throws SQLException, IOException {
        System.out.println("qa1234");



        // Tạo các Media để thử nghiệm
        Media testMedia1 = new Media(1, "Title 1", "Category 1", 100, 5, "Type 1");
        MediaHandler m1 = mock(MediaHandler.class);
        when(m1.getMedia()).thenReturn(testMedia1);

        Media testMedia2 = new Media(2, "Another ", "Category 2", 150, 3, "Type 2");
        MediaHandler m2 = mock(MediaHandler.class);
        when(m2.getMedia()).thenReturn(testMedia2);

        Media testMedia3 = new Media(3, "Title 3", "Category 1", 200, 7, "Type 1");
        MediaHandler m3 = mock(MediaHandler.class);
        when(m3.getMedia()).thenReturn(testMedia3);

        // Thêm các MediaHandler vào danh sách testItems
        testItems.add(m1);
        testItems.add(m2);
        testItems.add(m3);
        System.out.println("qa123");
        System.out.println("qa123");
    }

    @Test
    public void testFilterMediaByKeyWord_WhenKeywordExists() {
        String keyword = "Title";
        List<MediaHandler> filteredItems = FilterHelper.filterMediaByKeyWord(keyword, testItems);


        assertEquals(2, filteredItems.size());

        assertEquals("Title 1", filteredItems.get(0).getMedia().getTitle());
        assertEquals("Title 3", filteredItems.get(1).getMedia().getTitle());
    }

    @Test
    public void testFilterMediaByKeyWord_WhenKeywordNotExists() {
        String keyword = "";
        List<MediaHandler> filteredItems = FilterHelper.filterMediaByKeyWord(keyword, testItems);


        assertEquals(3, filteredItems.size());
    }


}
