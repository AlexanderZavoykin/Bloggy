package com.gmail.aazavoykin.util;

import com.gmail.aazavoykin.exception.InternalErrorType;
import com.gmail.aazavoykin.exception.InternalException;

public class ValidationUtils {

    public static boolean checkMatchingPassword(String password, String matchingPassword) {
        if (password.isEmpty() || matchingPassword.isEmpty()) {
            throw new InternalException(InternalErrorType.NOT_VALID_ARGUMENT);
        }
        return password.equals(matchingPassword);
    }

}
