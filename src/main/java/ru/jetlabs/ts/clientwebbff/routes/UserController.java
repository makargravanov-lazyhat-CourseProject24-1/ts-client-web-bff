package ru.jetlabs.ts.clientwebbff.routes;

import feign.FeignException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.jetlabs.ts.clientwebbff.client.authprovider.AuthProviderClient;
import ru.jetlabs.ts.clientwebbff.client.authprovider.LoginRequest;
import ru.jetlabs.ts.clientwebbff.client.authprovider.TokenResponse;
import ru.jetlabs.ts.clientwebbff.client.userservice.UserCreateForm;
import ru.jetlabs.ts.clientwebbff.client.userservice.UserResponseForm;
import ru.jetlabs.ts.clientwebbff.client.userservice.UserServiceClient;
import ru.jetlabs.ts.clientwebbff.client.userservice.UserUpdatePasswordForm;
import ru.jetlabs.ts.clientwebbff.dto.UserIdResponse;
import ru.jetlabs.ts.clientwebbff.service.CookieUtility;

import java.util.Objects;

@RestController
@RequestMapping("/bff/api/v1/")
@Slf4j
public class UserController {
    private final AuthProviderClient authProviderClient;
    private final UserServiceClient userServiceClient;
    private final CookieUtility cookieUtility;

    public UserController(AuthProviderClient authProviderClient, UserServiceClient userServiceClient, CookieUtility cookieUtility) {
        this.authProviderClient = authProviderClient;
        this.userServiceClient = userServiceClient;
        this.cookieUtility = cookieUtility;
    }

    @PostMapping("/open/login")
    ResponseEntity<?> login(@RequestBody LoginRequest loginData){
        try {
            ResponseEntity<TokenResponse> response = authProviderClient.login(loginData);
            if (response.getStatusCode().is2xxSuccessful()) {
                System.out.println(("token " + response));
                return ResponseEntity.ok()
                        .header(HttpHeaders.SET_COOKIE, cookieUtility
                                .create(Objects.requireNonNull(response.getBody()).token()).toString())
                        .body(new UserIdResponse(
                        Objects.requireNonNull(response.getBody()).userId()));
            } else {
                return ResponseEntity.status(404).body("Incorrect email or password");
            }
        }catch (FeignException e){
            if(e.status()==404){
                return ResponseEntity.status(404).body("Incorrect email or password");
            }
            return ResponseEntity.status(e.status()).build();
        }
    }
    @PostMapping("/open/register")
    ResponseEntity<?> register(@RequestBody UserCreateForm registerForm){
        try{
            userServiceClient.create(registerForm);
            return ResponseEntity.ok().body("Success");
        }catch (FeignException e){
            return ResponseEntity.status(e.status()).build();
        }
    }

    @PostMapping("/secured/change-password")
    ResponseEntity<Boolean> changePassword(HttpServletRequest request,
                                           @RequestBody UserUpdatePasswordForm form) {
        try {
            return userServiceClient.changePassword(
                    Long.parseLong(String.valueOf(request.getAttribute("extractedId"))), form);
        } catch (FeignException e) {
            return ResponseEntity.status(e.status()).build();
        }
    }
    @GetMapping("/secured/profile/my")
    ResponseEntity<UserResponseForm> myProfile(HttpServletRequest request){
        try {
            System.out.println("extracted id = "+request.getAttribute("extractedId"));
            ResponseEntity<UserResponseForm> result = userServiceClient.getById(Long.parseLong(
                    String.valueOf(request.getAttribute("extractedId"))));
            System.out.println(result);
            System.out.println("\n------------------------");

            return ResponseEntity.ok(result.getBody());
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }

}
