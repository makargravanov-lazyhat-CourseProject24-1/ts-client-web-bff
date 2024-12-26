package ru.jetlabs.ts.clientwebbff.client.agency;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "ts-agency-service", url = "http://ts-agency-service:8080/ts-agency-service/api/v1")
public interface AgencyClient {

    @PostMapping("/agency")
    ResponseEntity<?> registerAgency(@RequestBody AgencyRegisterForm registerAgencyForm);

    @GetMapping("/agency/{id}")
    ResponseEntity<?> getAgencyById(@PathVariable Long id);

    @GetMapping("/agency/my/{id}")
    ResponseEntity<?> getMyAgencies(@PathVariable Long id);

    @PostMapping("/update-bank")
    ResponseEntity<?> updateBankAccount(@RequestBody AgencyUpdateBankAccountForm updateBankAccountForm);

}
