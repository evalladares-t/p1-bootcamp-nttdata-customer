package com.nttdata.bootcamp.customer.Customer.expose;

import com.nttdata.bootcamp.customer.Customer.bussiness.CustomerService;
import com.nttdata.bootcamp.customer.Customer.model.Customer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@Slf4j
@RequestMapping("api/v1/customer")
public class CustomerController {

    @Autowired
    private CustomerService customerService;

    //list
    @GetMapping("")
    public Mono<ResponseEntity<Flux<Customer>>> findAll() {
        log.info("findAll>>>>>");
        return Mono.just(ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(customerService.findAll()));
    }

    //Create
    @PostMapping("")
    public Mono<ResponseEntity<Flux<Customer>>> create(@RequestBody Customer customer){
        log.info("Create>>>>>");
        return Mono.just(ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(customerService.create(customer)));
    }

    //Edit
    @PutMapping("")
    public Mono<ResponseEntity<Customer>> update(@RequestBody Customer customer) {
        log.info("update>>>>>");
        return customerService.update(customer)
                .flatMap(productUpdate -> Mono.just(ResponseEntity.ok(productUpdate)))
                .switchIfEmpty(Mono.just(ResponseEntity.notFound().build()));
    }

    //Detail
    @GetMapping("/{id}")
    public Mono<Customer> show(@PathVariable("id") String id) {
        log.info("byId>>>>>");
        return customerService.findById(id);
    }

    //Delete
    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Customer>> delete(@PathVariable("id") String id) {
        log.info("delete>>>>>");
        return customerService.remove(id)
                .flatMap(customer -> Mono.just(ResponseEntity.ok(customer)))
                .switchIfEmpty(Mono.just(ResponseEntity.notFound().build()));
    }
}
