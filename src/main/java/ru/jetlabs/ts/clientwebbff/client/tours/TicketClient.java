package ru.jetlabs.ts.clientwebbff.client.tours;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient(name="ts-tickets-service", url = "http://ts-tickets-service:8080/ts-tickets-service/api/v1/tickets")
public interface TicketClient {
    @PostMapping
    ResponseEntity<Ticket> registerTicket(@RequestBody RegisterTicketForm form);

    @GetMapping("/byuser/{id}")
    ResponseEntity<List<Ticket>> getTicketsByUserId(@PathVariable Long id);

    @GetMapping("/{id}/request-pay")
    ResponseEntity<String> pay(@PathVariable Long id);
}
