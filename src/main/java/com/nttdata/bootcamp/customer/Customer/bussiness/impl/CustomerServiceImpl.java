package com.nttdata.bootcamp.customer.Customer.bussiness.impl;

import com.nttdata.bootcamp.customer.Customer.bussiness.CustomerService;
import com.nttdata.bootcamp.customer.Customer.model.Customer;
import com.nttdata.bootcamp.customer.Customer.model.dto.MovementDTO;
import com.nttdata.bootcamp.customer.Customer.model.dto.RegisterMovementDTO;
import com.nttdata.bootcamp.customer.Customer.model.dto.ProductDTO;
import com.nttdata.bootcamp.customer.Customer.model.dto.RegisterProductoDTO;
import com.nttdata.bootcamp.customer.Customer.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.text.SimpleDateFormat;
import java.util.Date;

@Service
public class CustomerServiceImpl implements CustomerService {

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private WebClient webClientProduct;
    @Autowired
    private WebClient webClientMovement;

    @Override public Flux<Customer> create(Customer customer) {
        Flux<Customer> customerMono = customerRepository.findCustomerByTypeDocumentAndDocument(customer.getTypeDocument(), customer.getDocument());
        return customerMono.flatMap(p->{
            if(p.getTypeClient().equals("empresarial")){
                if(customer.getActive()==null){
                    customer.setActive(true);
                }
                customer.setOwner(false);
                customer.setCreatedAt(new Date());
                return customerRepository.save(customer);
            }
            return customerRepository.findById(p.getId());
        }).switchIfEmpty(Mono.defer(() -> {
            customer.setCreatedAt(new Date());
            customer.setOwner(true);
            if(customer.getActive()==null){
                customer.setActive(true);
            }
            return customerRepository.save(customer);}));
    }

    @Override
    public Flux<Customer> findAll() {
        return customerRepository.findAllByActiveIsTrue();
    }

    @Override
    public Mono<Customer> findById(String customerId) {
        return customerRepository.findById(customerId);
    }

    @Override
    public Mono<Customer> update(Customer customer) {
        return customerRepository.findById(customer.getId())
                .flatMap(productDB -> {
                    return customerRepository.save(customer);
                })
                .switchIfEmpty(Mono.empty());
    }

    @Override
    public Mono<Customer> remove(String customerId) {
        return customerRepository
                .findById(customerId)
                .flatMap(p -> {
                    p.setActive(false);
                    return customerRepository.save(p);
                });
    }

    @Override
    public Mono<Customer> registerProduct(RegisterProductoDTO productoDTO) {
        Mono<ProductDTO> objProduct = webClientProduct.get()
                .uri(uriBuilder -> uriBuilder.pathSegment(productoDTO.getProductId()).build())
                .retrieve()
                .bodyToMono(ProductDTO.class);

        return builProduct(productoDTO, objProduct);
    }

    @Override
    public Mono<Customer> registerMovement(RegisterMovementDTO rmovementDTO) {

        return customerRepository.findById(rmovementDTO.getCustomerId())
                .flatMap(c->{
                    c.getProduct().stream().filter(p->p.getId().equals(rmovementDTO.getProductId()) && p.getActive()).forEach(p->{
                        if((p.getTotalAmount()!=null &&(p.getTotalAmount()+rmovementDTO.getAmount()>=0)) ||
                                (p.getRestCredit()!=null && (p.getRestCredit()+rmovementDTO.getAmount()>=0) &&
                                        (p.getRestCredit()+rmovementDTO.getAmount()<=p.getTotalCredit()))){
                            if(p.getTypeProduct().equals("pasivo")){
                                if ((p.getRestMovements() ==null || p.getRestMovements()>0 && (p.getDateAction() == null || (p.getDateAction()!=null && (new SimpleDateFormat("MM").format(new Date())).equals(p.getDateAction().toString()))))) {
                                    MovementDTO movement = new MovementDTO();
                                    movement.setCreated(new Date());
                                    movement.setAmount(rmovementDTO.getAmount());
                                    movement.setTotalAmount(rmovementDTO.getAmount());
                                    if (p.getCommission() != null) {
                                        movement.setCommissionAmount(p.getCommission() * movement.getAmount());
                                        movement.setTotalAmount(movement.getAmount() - movement.getCommissionAmount());
                                        p.setTotalCommission(movement.getCommissionAmount()+p.getTotalCommission());
                                    }
                                    p.setTotalAmount(p.getTotalAmount()+rmovementDTO.getAmount());
                                    p.addMovement(movement);
                                }
                                if(p.getRestMovements()!=null){
                                    if(p.getRestMovements()>0) {
                                        p.setRestMovements(p.getRestMovements() - 1);
                                    }
                                }
                            }
                            if(p.getTypeProduct().equals("activo")){
                                MovementDTO movement = new MovementDTO();
                                movement.setCreated(new Date());
                                movement.setAmount(rmovementDTO.getAmount());
                                movement.setTotalAmount(rmovementDTO.getAmount());
                                p.setRestCredit(p.getRestCredit()+rmovementDTO.getAmount());
                                p.addMovement(movement);
                            }

                            RegisterMovementDTO objRegisterMovement = new RegisterMovementDTO();
                            objRegisterMovement.setAmount(rmovementDTO.getAmount());
                            objRegisterMovement.setCustomerId(rmovementDTO.getCustomerId());
                            objRegisterMovement.setProductId(rmovementDTO.getProductId());
                            sendMovement(objRegisterMovement).subscribe();
                        }
                    customerRepository.save(c).subscribe();
                    });
                    return customerRepository.findById(c.getId());
                });
    }

    public Mono<Customer> builProduct(RegisterProductoDTO productoDTO, Mono<ProductDTO> objProduct){
        return customerRepository.findById(productoDTO.getCustomerId()).flatMap(c->{
            return objProduct.flatMap(p->{
                p.setRestMovements(p.getMaxMovements());
                if (c.getTypeClient().equals("personal")) {
                    if(p.getTypeProduct().equals("pasivo") && c.getProduct().stream()
                            .filter(l->l.getName().equals(p.getName())).filter(l->l.getActive()).count() ==0){
                        p.setTotalAmount((float) 0);
                        p.setTotalCommission((float) 0);
                        c.addProduct(p);
                    }
                    if(p.getTypeProduct().equals("activo") && c.getProduct().stream()
                            .filter(l->l.getTypeProduct().equals("activo")).filter(l->l.getActive()).count() ==0){
                        p.setTotalCredit(productoDTO.getTotalCredit());
                        p.setRestCredit(productoDTO.getTotalCredit());
                        c.addProduct(p);
                    }
                }
                if (c.getTypeClient().equals("empresarial")) {
                    if(p.getTypeProduct().equals("pasivo") && p.getName().equals("Cuenta Corriente")){
                        p.setTotalAmount((float) 0);
                        c.addProduct(p);
                    }
                    if(p.getTypeProduct().equals("activo")){
                        p.setTotalCredit(productoDTO.getTotalCredit());
                        p.setRestCredit(productoDTO.getTotalCredit());
                        c.addProduct(p);
                    }
                }
                customerRepository.save(c).subscribe();
                return customerRepository.findById(c.getId());
            });
        });
    }


    public Mono<RegisterMovementDTO> sendMovement(RegisterMovementDTO rmovementDTO) {
        return  webClientMovement.post()
                .body(Mono.just(rmovementDTO), RegisterMovementDTO.class)
                .retrieve()
                .bodyToMono(RegisterMovementDTO.class);
    }

}