package com.gmail.aazavoykin.service;

import java.time.format.DateTimeFormatter;
import java.util.Objects;

public class ServiceUtils {

    public static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss");

    public static String cutStoryBody(String body, int limit) {
        Objects.requireNonNull(body);
        int length = body.length();
        return length > limit && limit > 0 ? body.substring(0, limit) + " . . ." : body;
    }

    public static boolean validateMatchingPasswords(String password, String matchingPassword) {
        return password.equals(matchingPassword);
    }

}
