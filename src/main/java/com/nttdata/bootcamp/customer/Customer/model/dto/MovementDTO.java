package com.nttdata.bootcamp.customer.Customer.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.annotation.Id;

import java.util.Date;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Slf4j
public class MovementDTO {

    @Id
    private String id = UUID.randomUUID().toString();
    private Date created;
    private Float amount;
    private Float commissionAmount;
    private Float totalAmount;
}
