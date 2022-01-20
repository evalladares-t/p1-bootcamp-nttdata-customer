package com.nttdata.bootcamp.customer.Customer.model.dto;

import lombok.Getter;

@Getter
public class RegisterProductoDTO {
    private String customerId;
    private String productId;
    private Float totalCredit;
}
