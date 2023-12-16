package controller;

import java.util.HashMap;
import java.util.Map;

public class DeliveryValidator {

    public static Map<String, String> validateDeliveryInfo(HashMap<String, String> info) {
        Map<String, String> errors = new HashMap<>();

        String name = info.get("name");
        if (validateName(name)) {
            errors.put("name", "Invalid name");
        }

        String phoneNumber = info.get("phone");
        if (validatePhoneNumber(phoneNumber)) {
            errors.put("phone", "Invalid phone number");
        }

        String address = info.get("address");
        if (validateAddress(address)) {
            errors.put("address", "Invalid address");
        }
        return errors;
    }

    public static boolean validatePhoneNumber(String phoneNumber) {
        if (phoneNumber == null || phoneNumber.trim().isEmpty()) return true;
        return !phoneNumber.matches("\\d{10}");
    }

    public static boolean validateName(String name) {
        if (name == null || name.trim().isEmpty()) return true;
        return !name.matches("^[a-zA-Z ]*$");
    }

    public static boolean validateAddress(String address) {
        if (address == null || address.trim().isEmpty()) return true;
        return !address.matches("^[a-zA-Z ]*$");
    }
}
