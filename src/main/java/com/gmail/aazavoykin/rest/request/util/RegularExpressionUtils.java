package com.gmail.aazavoykin.rest.request.util;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class RegularExpressionUtils {

    public static final String PASSWORD_REGEXP = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$";
    public static final String EMAIL_REGEXP = "^[a-zA-Z0-9_!#$%&’*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$";
    public static final String NICKNAME_REGEXP = "^[a-zA-Z0-9_-]{8,20}$";
    public static final String NICKNAME_MESSAGE = "Nickname should contain from 8 up to 20 symbols (letters, numbers, '_', '-'";
}
