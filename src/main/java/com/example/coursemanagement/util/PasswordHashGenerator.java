package com.example.coursemanagement.util;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class PasswordHashGenerator {
    public static void main(String[] args) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String hash = encoder.encode("Password@123");
        System.out.println("Hash: " + hash);

        boolean matches = encoder.matches("Password@123", hash);
        System.out.println("Matches: " + matches);
    }
}