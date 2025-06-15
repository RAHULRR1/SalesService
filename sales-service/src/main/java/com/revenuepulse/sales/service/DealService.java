package com.revenuepulse.sales.service;

import com.revenuepulse.sales.entity.Deal;
import com.revenuepulse.sales.repository.DealRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

@Service
public class DealService {

    private final DealRepository dealRepository;
    private final DealStatusPublisher dealStatusPublisher;
    public DealService(DealRepository dealRepository, DealStatusPublisher dealStatusPublisher) {
        this.dealStatusPublisher = dealStatusPublisher;
        this.dealRepository = dealRepository;
    }

    // Create a new deal
    public Mono<Deal> createDeal(Deal deal) {
        return dealRepository.save(deal);
    }

    // Update an existing deal
    public Mono<Deal> updateDeal(Integer dealId, Deal updatedDeal) {
        return dealRepository.findById(dealId)
                .flatMap(existingDeal -> {
                    existingDeal.setDealTitle(updatedDeal.getDealTitle());
                    existingDeal.setCustomerId(updatedDeal.getCustomerId());
                    existingDeal.setCustomerName(updatedDeal.getCustomerName());
                    existingDeal.setAmount(updatedDeal.getAmount());
                    existingDeal.setStatus(updatedDeal.getStatus());
                    existingDeal.setClosedDate(updatedDeal.getClosedDate());
                    return dealRepository.save(existingDeal);
                });
    }

    // Get a deal by ID
    public Mono<Deal> getDealById(Integer dealId) {
        return dealRepository.findById(dealId);
    }

    // Get all deals
    public Flux<Deal> getAllDeals() {
        return dealRepository.findAll();
    }

    public Mono<Void> deleteDeal(Integer id) {
        return dealRepository.deleteById(id);
    }


    public Mono<Deal> updateDealStatus(Integer id, String status) {
        return dealRepository.findById(id)
                .flatMap(existing -> {
                    existing.setStatus(status);
                    existing.setClosedDate(LocalDateTime.now());
                    return dealRepository.save(existing)
                            .flatMap(savedDeal -> {
                                if ("WON".equalsIgnoreCase(status)) {
                                    return dealStatusPublisher.publishWonStatus(savedDeal.getDealId())
                                            .thenReturn(savedDeal);
                                }
                                return Mono.just(savedDeal);
                            });
                });
    }
}

