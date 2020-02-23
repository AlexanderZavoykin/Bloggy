package com.gmail.aazavoykin.util;

import java.time.format.DateTimeFormatter;
import java.util.Objects;

public class ServiceUtils {

    public static boolean validateMatchingPasswords(String password, String matchingPassword) {
        return password.equals(matchingPassword);
    }

}
