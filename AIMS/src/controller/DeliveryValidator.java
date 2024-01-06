package controller;

import common.exception.InvalidDeliveryInfoException;

import java.util.HashMap;
import java.util.Map;

public class DeliveryValidator {

    public static void validateDeliveryInfo(HashMap<String, String> info) throws InterruptedException {

        if(validateName(info.get("name"))){
            throw  new InvalidDeliveryInfoException("Invalid name");
        }
        if(validatePhoneNumber(info.get("phone"))){
            throw  new InvalidDeliveryInfoException("Invalid phone number");
        }
        if(validateAddress(info.get("address"))){
            throw  new InvalidDeliveryInfoException("Invalid address");
        }
    }

    public static boolean validatePhoneNumber(String phoneNumber) {
        if (phoneNumber == null || phoneNumber.trim().isEmpty()) return true;
        return !phoneNumber.matches("^0[1-9][0-9]{8}$");
    }

    public static boolean validateName(String name) {
        if (name == null || name.trim().isEmpty()) return true;
        return !name.matches("^[a-zA-Z ]*$");
    }

    public static boolean validateAddress(String address) {
        if (address == null || address.trim().isEmpty()) return true;
        return !address.matches("^[.0-9a-zA-Z\\s,-]+$");
    }
}
