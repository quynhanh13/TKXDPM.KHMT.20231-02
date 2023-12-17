package controller;

import java.util.List;

import entity.cart.Cart;
import entity.cart.CartMedia;
import entity.media.Media;

/**
 * This class is the base controller for our AIMS project
 * @author nguyenlm
 */
//funtional cohesion
public class BaseController {
    /**
     * Phương thức này kiểm tra xem Media có trong giỏ hàng không, nếu có sẽ trả về CartMedia, ngược lại trả về null.
     * VI PHẠM NGUYÊN TẮC SOLID:
     * - VI PHẠM Single Responsibility Principle (Nguyên tắc đơn trách nhiệm):
     *   + Lớp BaseController không chỉ là một controller cơ bản mà còn trực tiếp thực hiện logic liên quan đến Cart và Media.
     *   + Nó không chỉ kiểm tra xem Media có trong Cart hay không mà còn trả về CartMedia, không giữ cho lớp này có trách nhiệm đơn giản.
     * 
    /**
     * The method checks whether the Media in Cart, if it were in, we will return the CartMedia else return null
     * @param media
     * @return CartMedia or null
     */
    public CartMedia checkMediaInCart(Media media){
        // data coupling
        return Cart.getCart().checkMediaInCart(media);
    }
    /**
     * Phương thức này lấy danh sách các mục trong giỏ hàng
     * VI PHẠM NGUYÊN TẮC SOLID:
     * - VI PHẠM Single Responsibility Principle (Nguyên tắc đơn trách nhiệm):
     *   + Lớp BaseController không chỉ là một controller cơ bản mà còn trực tiếp thực hiện logic liên quan đến Cart và Media.
     *   + Nó không chỉ lấy danh sách các mục trong Cart mà còn trả về danh sách các đối tượng CartMedia.
     * 
    /**
     * This method gets the list of items in cart
     * @return List[CartMedia]
     */
    public List getListCartMedia(){
        // data coupling
        return Cart.getCart().getListMedia();
    }
}
