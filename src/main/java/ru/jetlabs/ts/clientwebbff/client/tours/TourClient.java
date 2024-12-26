package ru.jetlabs.ts.clientwebbff.client.tours;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(name = "ts-tour-processing-service", url = "http://ts-tour-processing-service:8080/ts-tour-processing-service/api/v1/tour")
public interface TourClient {

    @GetMapping
    ResponseEntity<Tour> getAll();
}
