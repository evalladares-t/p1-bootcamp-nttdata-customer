package com.nttdata.bootcamp.customer.Customer.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Slf4j
public class RegisterMovementDTO {

    private String customerId;
    private String productId;
    private Float amount;
}
