package com.revenuepulse.sales.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;

@Data
@Table("\"RevenuePulse\".customer")
public class Customer {

    @Id
    private Integer customerId;

    private String customerName;
    private String customerEmail;
    private String customerPhone;
    private String customerType;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
