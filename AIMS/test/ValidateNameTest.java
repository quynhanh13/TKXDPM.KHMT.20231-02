//import static org.junit.Assert.assertEquals;
//import static org.junit.jupiter.api.Assertions.fail;
//
//import controller.PlaceOrderController;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.params.ParameterizedTest;
//import org.junit.jupiter.params.provider.CsvSource;
//
//class ValidateNameTest {
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
//            "TranBach, false",
//            "Tran Xuan Bach, false",
//            "Tran Bach 1, true",
//            "166621877, true",
//            "@Tran Xuan Bach, true"
//    })
//
//    public void testValidateName(String name, boolean expected) {
//        boolean isValid = placeOrderController.validateName(name);
//        assertEquals(expected, isValid);
//    }
//}
