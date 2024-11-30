package ru.jetlabs.ts.clientwebbff.service;

import feign.FeignException;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import ru.jetlabs.ts.clientwebbff.client.authprovider.AuthProviderClient;
import ru.jetlabs.ts.clientwebbff.client.authprovider.TokenRequest;
import ru.jetlabs.ts.clientwebbff.client.authprovider.TokenResponse;
import ru.jetlabs.ts.clientwebbff.entities.ValidationResult;

import java.util.Objects;

@Service
public class AuthService {
    private final AuthProviderClient authProviderClient;

    public AuthService(AuthProviderClient authProviderClient) {
        this.authProviderClient = authProviderClient;
    }

    public ValidationResult validate(String token) throws FeignException{
        try {
            ResponseEntity<?> response = authProviderClient.validateToken(
                    new TokenRequest(token, false));
            if (response.getStatusCode().is2xxSuccessful()) {
                ResponseEntity<TokenResponse> castedResponse =
                        (ResponseEntity<TokenResponse>) response;
                return new ValidationResult(true,
                        Objects.requireNonNull(castedResponse.getBody()).userId(),
                        castedResponse.getBody().token());
            } else {
                return new ValidationResult(false,
                        -1L,
                        "");
            }
        } catch (FeignException e) {
            if(e.status()==500){
                throw e;
            }
            return new ValidationResult(false,
                    -1L,
                    "");
        }
    }

    public ValidationResult validateConfirmed(String token) throws FeignException{
        try {
            ResponseEntity<?> response = authProviderClient.validateToken(
                    new TokenRequest(token, true));
            if (response.getStatusCode().is2xxSuccessful()) {
                ResponseEntity<TokenResponse> castedResponse =
                        (ResponseEntity<TokenResponse>) response;
                return new ValidationResult(
                        Objects.requireNonNull(castedResponse.getBody()).emailConfirmed(),
                        castedResponse.getBody().userId(),
                        castedResponse.getBody().token());
            } else {
                return new ValidationResult(false,
                        -1L,
                        "");
            }
        } catch (FeignException e) {
            if(e.status()==500){
                throw e;
            }
            return new ValidationResult(false,
                    -1L,
                    "");
        }
    }
}
