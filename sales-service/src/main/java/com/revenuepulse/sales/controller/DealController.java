package com.revenuepulse.sales.controller;

import com.revenuepulse.sales.entity.Deal;
import com.revenuepulse.sales.service.CustomerService;
import com.revenuepulse.sales.service.DealService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/deals")
public class DealController {

    private final DealService dealService;

    private final CustomerService customerService;

    public DealController(DealService dealService, CustomerService customerService) {
        this.dealService = dealService;
        this.customerService = customerService;
    }

    // Create a new deal
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<Deal> createDeal(@RequestBody Deal deal) {
        return dealService.createDeal(deal);
    }

    // Update an existing deal
    @PutMapping("/{id}")
    public Mono<Deal> updateDeal(@PathVariable Integer id, @RequestBody Deal deal) {
        return dealService.updateDeal(id, deal);
    }

    // Get all deals
    @GetMapping
    public Flux<Deal> getAllDeals() {
        return dealService.getAllDeals();
    }

    // Get a single deal by ID
    @GetMapping("/{id}")
    public Mono<Deal> getDealById(@PathVariable Integer id) {
        return dealService.getDealById(id);
    }


    @GetMapping("/customerDetails")
    public Flux<Map<String, Object>> getAllDealsWithCustomers() {
        return dealService.getAllDeals()
                .flatMap(deal -> customerService.getCustomerById(deal.getCustomerId())
                        .map(customer -> {
                            Map<String, Object> dealWithCustomer = new HashMap<>();
                            dealWithCustomer.put("deal", deal);
                            Map<String, Object> customerDetails = new HashMap<>();
                            customerDetails.put("customerId", customer.getCustomerId());
                            customerDetails.put("type", customer.getCustomerType());
                            dealWithCustomer.put("customer", customerDetails);
                            return dealWithCustomer;
                        }));
    }


    @DeleteMapping("/{id}")
    public Mono<Void> deleteDeal(@PathVariable Integer id) {
        return dealService.deleteDeal(id);
    }

    @PatchMapping("/{id}/status")
    public Mono<Deal> updateDealStatus(@PathVariable Integer id, @RequestBody Map<String, String> request) {
        String status = request.get("status");
        return dealService.updateDealStatus(id, status);
    }
}
