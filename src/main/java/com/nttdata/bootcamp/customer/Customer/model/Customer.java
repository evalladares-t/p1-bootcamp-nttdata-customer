package com.nttdata.bootcamp.customer.Customer.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.Date;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Slf4j
@Document(collection = "customer")
public class Customer {

    @Id
    private String id = UUID.randomUUID().toString();
    @Field(name = "first_name")
    private String firstName;
    @Field(name = "last_name")
    private String lastName;
    private String email;
    private String phone;
    @Field(name = "document_type")
    private String documentType;
    @Field(name = "document_number")
    private String documentNumber;
    @Field(name = "customer_type")
    private String customerType;
    @Field(name = "created_by")
    private String createdBy;
    @Field(name = "is_owner")
    private Boolean owner;
    @Field(name = "is_active")
    private Boolean active;
    @Field(name = "created_at")
    private Date createdAt;

}