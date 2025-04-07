package com.dev.customer;


import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/customers")
public class CustomerController {

    private final CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @GetMapping
    private List<Customer> getCustomers() {
        return customerService.getAllCustomers();
    }

    @GetMapping("/{customersId}")
    private Customer getCustomerById(
            @PathVariable("customersId")
            Integer customersId
    ) {
        return customerService.getCustomerById(customersId);
    }

    @PostMapping
    private void insertCustomers(
            @RequestBody
            CustomerRegistrationRequest customersRegistrationRequest
    ) {
        customerService.addCustomer(customersRegistrationRequest);
    }

    @GetMapping("/exists/{email}")
    private boolean existsPersonWithEmail(
            @PathVariable("email")
            String email
    ) {
        return customerService.doesCustomerExistWithEmail(email);
    }

    @GetMapping("/existsID/{id}")
    private boolean existsPersonWithId(
            @PathVariable("id")
            Integer id
    ) {
        return customerService.doesCustomerExistWithId(id);
    }

    @DeleteMapping("/{customersId}")
    private String deleteCustomerById(
            @PathVariable("customersId")
            Integer customersId
    ) {
        customerService.deleteCustomerById(customersId);
        return "Customer deleted successfully";
    }

    @PutMapping("/{customerId}")
    private void updateCustomerById(
            @PathVariable("customerId")
            Integer customerId,
            @RequestBody
            CustomerUpdateRequest updateRequest
    ) {
        customerService.updateCustomer(
                customerId,
                updateRequest
        );
    }

}
