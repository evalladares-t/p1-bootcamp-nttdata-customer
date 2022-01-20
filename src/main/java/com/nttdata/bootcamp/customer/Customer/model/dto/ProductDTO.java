package com.nttdata.bootcamp.customer.Customer.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.annotation.Id;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Slf4j
public class ProductDTO {

    @Id
    private String id = UUID.randomUUID().toString();
    private String typeProduct;
    private String name;
    private Float commission;
    private Integer maxMovements;
    private Integer restMovements;
    private Integer dateAction;
    private Boolean active;
    private Float totalAmount;
    private Float totalCommission;
    private Float totalCredit;
    private Float restCredit;
    private List<MovementDTO> movement = new ArrayList<>();

    public void addMovement(MovementDTO m) {
        this.movement.add(m);
    }
}