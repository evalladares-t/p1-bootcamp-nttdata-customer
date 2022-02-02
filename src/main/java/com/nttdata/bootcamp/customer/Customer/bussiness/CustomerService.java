package com.nttdata.bootcamp.customer.Customer.bussiness;


import com.nttdata.bootcamp.customer.Customer.model.Customer;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public interface CustomerService {
    Flux<Customer> create(Customer customer);
    Flux<Customer> findAll();
    Mono<Customer> findByPhone(String phone);
    Mono<Customer> findById(String customerId);
    Mono<Customer> update(Customer customer);
    Mono<Customer> remove(String customerId);
}
