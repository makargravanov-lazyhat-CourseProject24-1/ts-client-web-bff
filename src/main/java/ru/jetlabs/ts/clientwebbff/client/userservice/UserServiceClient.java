package ru.jetlabs.ts.clientwebbff.client.userservice;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "ts-user-service", url = "http://ts-user-service:8080/ts-user-service/api/v1")
public interface UserServiceClient {
    @GetMapping("/{id}")
    ResponseEntity<UserResponseForm> getById(@PathVariable Long id);
    @PostMapping("/create")
    ResponseEntity<?> create(@RequestBody UserCreateForm form);
    @PostMapping("/{id}/change-password")
    ResponseEntity<Boolean> changePassword(@PathVariable Long id,
                                           @RequestBody UserUpdatePasswordForm form);
}
