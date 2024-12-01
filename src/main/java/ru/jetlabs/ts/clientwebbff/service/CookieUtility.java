package ru.jetlabs.ts.clientwebbff.service;

import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Service;

@Service
public class CookieUtility {
    public ResponseCookie create(String token){
        return ResponseCookie.from("jwt")
                .value(token)
                .path("/")
                .httpOnly(true)
                .secure(true)
                .build();
    }
}
