package ru.jetlabs.ts.clientwebbff.routes;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.jetlabs.ts.clientwebbff.client.agency.*;
import ru.jetlabs.ts.clientwebbff.client.tours.RegisterTicketForm;
import ru.jetlabs.ts.clientwebbff.client.tours.TicketClient;
import ru.jetlabs.ts.clientwebbff.client.tours.Tour;
import ru.jetlabs.ts.clientwebbff.client.tours.TourClient;

@RestController
@RequestMapping("/bff/api/v1/")
public class AgencyController {
    private final AgencyClient agencyClient;
    private final TourClient tourClient;
    private final TicketClient ticketClient;

    public AgencyController(AgencyClient agencyClient, TourClient tourClient, TicketClient ticketClient) {
        this.agencyClient = agencyClient;
        this.tourClient = tourClient;
        this.ticketClient = ticketClient;
    }

    @PostMapping("/secured/agency/{name}")
    ResponseEntity<?> registerAgency(HttpServletRequest request, @PathVariable String name) {
        return ResponseEntity.ok(agencyClient.registerAgency(new AgencyRegisterForm((Long) request.getAttribute("extractedId"), name)).getBody());
    }

    @GetMapping("/secured/tours")
    ResponseEntity<?> getAllTours(){
        return ResponseEntity.ok(tourClient.getAll().getBody());
    }
    @PostMapping("/secured/regtour")
    ResponseEntity<?> register(HttpServletRequest request, @RequestBody Tour form){
        return ResponseEntity.ok(ticketClient.registerTicket(new RegisterTicketForm(form, (Long) request.getAttribute("extractedId"))).getBody());
    }
    @GetMapping("/secured/my")
    ResponseEntity<?> getAllMyTickets(HttpServletRequest request){
        return ResponseEntity.ok(ticketClient.getTicketsByUserId((Long) request.getAttribute("extractedId")));
    }
    @GetMapping("/secured/pay/{id}")
    ResponseEntity<?> pay(@PathVariable Long id){
        return ResponseEntity.ok(ticketClient.pay(id));
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
