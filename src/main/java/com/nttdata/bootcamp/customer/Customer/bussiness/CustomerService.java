package com.nttdata.bootcamp.customer.Customer.bussiness;


import com.nttdata.bootcamp.customer.Customer.model.Customer;
import com.nttdata.bootcamp.customer.Customer.model.dto.RegisterMovementDTO;
import com.nttdata.bootcamp.customer.Customer.model.dto.RegisterProductoDTO;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface CustomerService {
    Flux<Customer> create(Customer customer);
    Flux<Customer> findAll();
    Mono<Customer> findById(String customerId);
    Mono<Customer> update(Customer customer);
    Mono<Customer> remove(String customerId);
    Mono<Customer> registerProduct(RegisterProductoDTO productoDTO);
    Mono<Customer> registerMovement(RegisterMovementDTO movementDTO);
}
