package com.revenuepulse.sales.entity;


import lombok.Data;

@Data
public class InvoiceDetails {
    public Deal deal;
    public Customer customer;
}
