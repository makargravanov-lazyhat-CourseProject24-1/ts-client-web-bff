package ru.jetlabs.ts.clientwebbff.routes;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.jetlabs.ts.clientwebbff.client.agency.*;

@RestController
@RequestMapping("/bff/api/v1/")
public class AgencyController {
    private final AgencyClient agencyClient;

    public AgencyController(AgencyClient agencyClient) {
        this.agencyClient = agencyClient;
    }

    @PostMapping("/secured/agency/{name}")
    ResponseEntity<?> registerAgency(HttpServletRequest request, @PathVariable String name) {
        return ResponseEntity.ok(agencyClient.registerAgency(new AgencyRegisterForm((Long) request.getAttribute("extractedId"), name)).getBody());
    }

    @GetMapping("/agency/{id}")
    ResponseEntity<?> getAgencyById(@PathVariable Long id) {
        return ResponseEntity.ok(agencyClient.getAgencyById(id).getBody());
    }

    @GetMapping("/secured/agency/my")
    ResponseEntity<?> getMyAgencies(HttpServletRequest request) {
        return ResponseEntity.ok(agencyClient.getMyAgencies((Long) request.getAttribute("extractedId")).getBody());
    }

    @PostMapping("/secured/update-bank")
    ResponseEntity<?> updateBankAccount(@RequestBody AgencyUpdateBankAccountForm updateBankAccountForm) {
        return ResponseEntity.ok(agencyClient.updateBankAccount(updateBankAccountForm).getBody());
    }
}
