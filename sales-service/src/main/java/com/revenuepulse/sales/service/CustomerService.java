package com.revenuepulse.sales.service;

import com.revenuepulse.sales.entity.Customer;
import com.revenuepulse.sales.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class CustomerService {

    private final CustomerRepository customerRepository;

    public Mono<Customer> createCustomer(Customer customer) {
        return customerRepository.save(customer);
    }

    public Mono<Customer> getCustomerById(Integer customerId) {
        return customerRepository.findById(customerId);
    }

    public Flux<Customer> getAllCustomers() {
        return customerRepository.findAll();
    }

    public Mono<Void> deleteCustomer(Integer customerId) {
        return customerRepository.deleteById(customerId);
    }

    public Mono<Customer> updateCustomer(Integer customerId, Customer updatedCustomer) {
        return customerRepository.findById(customerId)
                .flatMap(existingCustomer -> {
                    existingCustomer.setCustomerName(updatedCustomer.getCustomerName());
                    existingCustomer.setCustomerEmail(updatedCustomer.getCustomerEmail());
                    existingCustomer.setCustomerPhone(updatedCustomer.getCustomerPhone());
                    existingCustomer.setCustomerType(updatedCustomer.getCustomerType()); // Don't forget type!
                    existingCustomer.setUpdatedAt(java.time.LocalDateTime.now()); // update the timestamp

                    return customerRepository.save(existingCustomer);
                });
    }


}
