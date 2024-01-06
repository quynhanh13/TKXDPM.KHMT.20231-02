//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import views.screen.home.HomeScreenHandler;
//import views.screen.home.MediaHandler;
//import controller.HomeController;
//import entity.media.Media;
//import java.io.IOException;
//import java.sql.SQLException;
//import java.util.ArrayList;
//import java.util.List;
//import static org.junit.jupiter.api.Assertions.assertEquals;
//
//class MockHomeController extends HomeController {
//    @Override
//    public List<Media> getAllMedia() throws SQLException {
//        List<Media> mockMediaList = new ArrayList<>();
//        // Tạo danh sách media giả mạo
//        mockMediaList.add(new Media(/* thông tin media giả mạo */));
//        return mockMediaList;
//    }
//}
//
//public class SearchProductTest {
//    private HomeScreenHandler homeScreenHandler;
//
//    @BeforeEach
//    public void setup() throws IOException, SQLException {
//        // Tạo đối tượng giả mạo và sử dụng nó trong test
//        homeScreenHandler = new HomeScreenHandler(new MockStage(), "home.fxml");
//        homeScreenHandler.setBController(new MockHomeController());
//    }
//
//    @Test
//    public void testSearchButtonClicked() throws SQLException, IOException {
//        // Mocking a search scenario
//        String searchText = "book"; // Text cần tìm kiếm
//        List<MediaHandler> mockMediaList = createMockMediaList();
//
//        List<MediaHandler> filteredItems = homeScreenHandler.filterMediaByKeyWord(searchText, mockMediaList);
//
//        // Kiểm tra xem danh sách được lọc có chứa số lượng phần tử mong muốn hay không
//        assertEquals(1, filteredItems.size()); // Giả sử có 1 phần tử với tiêu đề "book" trong danh sách giả mạo
//    }
//
//    private List<MediaHandler> createMockMediaList() throws SQLException, IOException {
//        // Tạo danh sách giả mạo của MediaHandler để sử dụng trong test
//        List<MediaHandler> mockMediaList = new ArrayList<>();
//        mockMediaList.add(new MediaHandler("a", new Media(), homeScreenHandler)); // Ví dụ với "Book Title" và giá 20
//        // Thêm các phần tử khác nếu cần
//        return mockMediaList;
//    }
//
//    // Các test cases khác nếu có
//}
