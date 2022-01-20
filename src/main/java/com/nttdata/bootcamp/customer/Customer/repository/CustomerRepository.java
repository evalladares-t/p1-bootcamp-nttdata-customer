package com.nttdata.bootcamp.customer.Customer.repository;

import com.nttdata.bootcamp.customer.Customer.model.Customer;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface CustomerRepository extends ReactiveMongoRepository<Customer, String> {
    Flux<Customer> findAllByActiveIsTrue();
    Flux<Customer> findCustomerByTypeDocumentAndDocument(String typeDocuemnt, String document);
}
