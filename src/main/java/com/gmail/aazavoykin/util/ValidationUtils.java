package com.gmail.aazavoykin.util;

import com.gmail.aazavoykin.exception.InternalErrorType;
import com.gmail.aazavoykin.exception.InternalException;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ValidationUtils {

    public static void checkMatchingPassword(String password, String matchingPassword) {
        if (!password.equals(matchingPassword)) {
            throw new InternalException(InternalErrorType.PASSWORDS_NOT_MATCH);
        }
    }
}
