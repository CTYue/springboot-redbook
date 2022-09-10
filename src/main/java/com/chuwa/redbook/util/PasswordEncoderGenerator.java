package com.chuwa.redbook.util;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * @author b1go
 * @date 6/26/22 4:28 PM
 */
public class PasswordEncoderGenerator {
    public static void main(String[] args) {
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        System.out.println(bCryptPasswordEncoder.encode("chuwa"));
    }
}
