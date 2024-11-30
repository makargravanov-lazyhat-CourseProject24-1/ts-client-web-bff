package ru.jetlabs.ts.clientwebbff.service;

import jakarta.servlet.http.Cookie;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class CookieUtility {
    public String create(String token){
        Cookie newJwtCookie = new Cookie("jwt",
                Objects.requireNonNull(token));
        newJwtCookie.setHttpOnly(true);
        newJwtCookie.setPath("/");
        return newJwtCookie.toString();
    }
}
