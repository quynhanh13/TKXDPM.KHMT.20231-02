//import static org.junit.Assert.assertEquals;
//import static org.junit.jupiter.api.Assertions.fail;
//
//import controller.PlaceOrderController;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.params.ParameterizedTest;
//import org.junit.jupiter.params.provider.CsvSource;
//
//class ValidateAddressTest {
//
//    private PlaceOrderController placeOrderController;
//
//    @BeforeEach
//    public void SetUp(){
//        placeOrderController = new PlaceOrderController();
//    }
//
//    @ParameterizedTest
//    @CsvSource({
//            " , true",
//            ", true",
//            "Ha Noi, false",
//            "Ha Noi 1, true",
//            "@Giai Phong, true"
//    })
//
//    public void testValidateAddress(String address, boolean expected) {
//        boolean isValid = placeOrderController.validateAddress(address);
//        assertEquals(expected, isValid);
//    }
//}
