//import static org.junit.Assert.assertEquals;
//import static org.junit.jupiter.api.Assertions.fail;
//
//import controller.PlaceOrderController;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.params.ParameterizedTest;
//import org.junit.jupiter.params.provider.CsvSource;
//
//class ValidatePhoneNumberTest {
//
//	private PlaceOrderController placeOrderController;
//
//	@BeforeEach
//	public void SetUp(){
//		placeOrderController = new PlaceOrderController();
//	}
//
//	@ParameterizedTest
//	@CsvSource({
//			" , true",
//			", true",
//			"0987654321, false",
//			"098738363a, true",
//			"bachahahah, true",
//			"0927292, true",
//			"92827277!, true"
//	})
//
//	public void testValidatePhoneNumber(String phone, boolean expected) {
//		boolean isValid = placeOrderController.validatePhoneNumber(phone);
//		assertEquals(expected, isValid);
//	}
//}
