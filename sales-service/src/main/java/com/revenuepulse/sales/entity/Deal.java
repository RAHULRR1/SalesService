package com.revenuepulse.sales.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;

@Data
@Table("\"RevenuePulse\".deal")
public class Deal {

    @Id
    private Integer dealId;

    private String dealTitle;
    private Integer customerId;
    private String customerName;
    private Double amount;
    private String status;
    private LocalDateTime createdDate;
    private LocalDateTime closedDate;

}
