package com.revenuepulse.sales.service;


import com.revenuepulse.sales.entity.Customer;
import com.revenuepulse.sales.entity.Deal;
import com.revenuepulse.sales.entity.InvoiceDetails;
import com.revenuepulse.sales.repository.CustomerRepository;
import com.revenuepulse.sales.repository.DealRepository;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import java.util.concurrent.CompletableFuture;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class DealStatusPublisher {

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final DealRepository dealRepository;
    private static final String TOPIC = "deal-status";
    private final CustomerRepository customerRepository;
    private final ObjectMapper objectMapper;


    public DealStatusPublisher(KafkaTemplate<String, String> kafkaTemplate,
                               DealRepository dealRepository,
                               CustomerRepository customerRepository, ObjectMapper objectMapper) {
        this.kafkaTemplate = kafkaTemplate;
        this.dealRepository =  dealRepository;
        this.customerRepository =  customerRepository;
        this.objectMapper = objectMapper;
    }


    public Mono<Void> publishWonStatus(Integer dealId) {
        return dealRepository.findById(dealId)
                .zipWhen(deal -> customerRepository.findById(deal.getCustomerId()))
                .flatMap(tuple -> {
                    Deal deal = tuple.getT1();
                    Customer customer = tuple.getT2();

                    InvoiceDetails message = new InvoiceDetails();
                    message.setDeal(deal);
                    message.setCustomer(customer);

                    // Correctly handle the CompletableFuture conversion
                    return Mono.fromFuture(() -> CompletableFuture.supplyAsync(() -> {
                                try {
                                    String json = objectMapper.writeValueAsString(message);
                                    return kafkaTemplate.send(TOPIC, json).get();
                                } catch (Exception e) {
                                    throw new RuntimeException("Failed to send message", e);
                                }
                            }))
                            .doOnSuccess(result -> System.out.println("Message sent successfully"))
                            .doOnError(e -> System.err.println("Failed to send message: " + e.getMessage()))
                            .then(); // convert to Mono<Void>
                });
    }


}