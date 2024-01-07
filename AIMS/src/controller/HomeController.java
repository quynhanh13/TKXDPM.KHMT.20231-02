package controller;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import entity.cart.Cart;
import entity.media.Media;
import views.screen.home.MediaHandler;

/**
 * This class controls the flow of events in homescreen
 * @author nguyenlm
 */
//funtional cohesion
public class HomeController extends BaseController{

    /** Phương thức này lấy tất cả các đối tượng Media từ cơ sở dữ liệu và trả về chúng.
     * VI PHẠM NGUYÊN TẮC SOLID:
     * - VI PHẠM Single Responsibility Principle (Nguyên tắc đơn trách nhiệm):
     *   + Lớp này không chỉ điều khiển luồng logic mà còn trực tiếp thực hiện truy vấn cơ sở dữ liệu để lấy dữ liệu Media.
     *   + Điều này làm cho lớp này có nhiều trách nhiệm hơn cần thiết.
     *
     * - VI PHẠM Dependency Inversion Principle (Nguyên tắc đảo ngược phụ thuộc):
     *   + Lớp HomeController tạo trực tiếp một đối tượng Media bên trong phương thức để lấy dữ liệu từ cơ sở dữ liệu.
     *   + Điều này tạo ra mối liên kết cứng giữa HomeController và lớp Media, không tuân theo nguyên tắc đảo ngược phụ thuộc.
     *
     /**
     * this method gets all Media in DB and return back to home to display
     * @return List[Media]
     * @throws SQLException
     */
    public List getAllMedia() throws SQLException{
        // data coupling
        return new Media().getAllMedia();
    }
    public List<Media> filterMediaByKeyWord(String keyword, List<Media> items) {
        List<Media> filteredItems = new ArrayList<>();
        for (Media item : items) {
            if (item.getTitle().toLowerCase().contains(keyword)) {
                filteredItems.add(item);
            }
        }
        return filteredItems;
    }

}
