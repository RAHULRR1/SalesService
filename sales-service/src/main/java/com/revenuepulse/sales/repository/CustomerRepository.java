package com.revenuepulse.sales.repository;

import com.revenuepulse.sales.entity.Customer;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerRepository extends ReactiveCrudRepository<Customer, Integer> {
    // Additional query methods (if needed) can go here
}
