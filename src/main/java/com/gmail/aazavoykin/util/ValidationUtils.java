package com.gmail.aazavoykin.util;

public class ValidationUtils {

    public static boolean checkMatchingPassword(String password, String matchingPassword) {
        return password.equals(matchingPassword);
    }

}
