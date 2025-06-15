
package com.revenuepulse.sales.repository;

import com.revenuepulse.sales.entity.Deal;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DealRepository extends ReactiveCrudRepository<Deal, Integer> {
    // Additional query methods can be defined if needed
}
