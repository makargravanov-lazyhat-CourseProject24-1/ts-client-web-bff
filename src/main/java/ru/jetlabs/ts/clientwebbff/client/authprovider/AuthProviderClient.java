package ru.jetlabs.ts.clientwebbff.client.authprovider;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "ts-auth-provider", url = "http://ts-auth-provider:8080/ts-auth-provider/api/v1")
public interface AuthProviderClient {
    @PostMapping("/login")
    ResponseEntity<?> login(@RequestBody LoginRequest loginRequest);
    @PostMapping("/validate")
    ResponseEntity<?> validateToken(@RequestBody TokenRequest tokenRequest);
}
