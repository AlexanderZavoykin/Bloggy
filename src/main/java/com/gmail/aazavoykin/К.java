package com.gmail.aazavoykin;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class К {

    public static void main(String[] args) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        System.out.println(encoder.encode("testtest"));
    }

}
