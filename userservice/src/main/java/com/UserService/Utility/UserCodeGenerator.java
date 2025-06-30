//package com.UserService.Utility;
//
//import com.UserService.Entity.Address;
//
//public class UserCodeGenerator {
//
//    public static String generateUserCode(String env, Address address) {
//        if (env == null || address == null) {
//            throw new IllegalArgumentException("Environment and address must not be null");
//        }
//
//        String stateCode = (address.getState() != null && address.getState().length() >= 3)
//                ? address.getState().substring(0, 3).toLowerCase()
//                : "unk";
//
//        String timeComponent = String.valueOf(System.currentTimeMillis()).substring(8); // last 4-5 digits
//        String zip = address.getPostalCode() != null ? address.getPostalCode() : "000000";
//
//        return String.format("%s-%s-%s-%s", env.toLowerCase(), stateCode, timeComponent, zip);
//    }
//}
