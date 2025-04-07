package com.dev.customer;


import com.dev.exception.DuplicateResourceException;
import com.dev.exception.RequestValidationException;
import com.dev.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomerService {

    private final CustomerDao customerDao;

    public CustomerService(
            @Qualifier("jdbc")
            CustomerDao customerDao
    ) {
        this.customerDao = customerDao;
    }

    public List<Customer> getAllCustomers() {
        return customerDao.getAllCustomers();
    }

    public Customer getCustomerById(Integer id) {
        return customerDao.getCustomerById(id)
                          .orElseThrow(() -> new ResourceNotFoundException("customer with id [%d] not found".formatted(id)));
    }

    public void addCustomer(CustomerRegistrationRequest customersRegistrationRequest) {

//        check if email exists
        String email = customersRegistrationRequest.email();
        if (customerDao.existsPersonWithEmail(email)) {
            throw new DuplicateResourceException("email already exists");
        }
//        add
        Customer customer = new Customer(
                customersRegistrationRequest.name(),
                customersRegistrationRequest.email(),
                customersRegistrationRequest.phone(),
                customersRegistrationRequest.age()
        );
        customerDao.insertCustomers(customer);
    }

    public boolean doesCustomerExistWithEmail(String email) {
        if (email == null || email.isBlank()) {
            throw new IllegalArgumentException("Email must not be null or blank");
        }
        return customerDao.existsPersonWithEmail(email);
    }

    public boolean doesCustomerExistWithId(Integer id) {
        if (id == null || id < 0) {
            throw new IllegalArgumentException("Email must not be null or blank");
        }
        return customerDao.existsPersonWithId(id);
    }

    public void deleteCustomerById(Integer id) {
        if (!customerDao.existsPersonWithId(id)) {
            throw new ResourceNotFoundException("customer with id [%d] not found".formatted(id));
        }
        customerDao.deleteCustomerById(id);
    }

    public void updateCustomer(Integer id, CustomerUpdateRequest updateRequest) {
        Customer customer = customerDao.getCustomerById(id)
                                       .orElseThrow(() -> new ResourceNotFoundException("Customer with id [%d] not found".formatted(id)));

        boolean changes = false;

        if (updateRequest.name() != null && !updateRequest.name()
                                                          .equals(customer.getName())) {
            customer.setName(updateRequest.name());
            changes = true;
        }
        if (updateRequest.email() != null && !updateRequest.email()
                                                           .equals(customer.getEmail())) {
            if (customerDao.existsPersonWithEmail(updateRequest.email())) {
                throw new DuplicateResourceException("email already exists");
            }
            customer.setEmail(updateRequest.email());
            changes = true;
        }

        if (updateRequest.age() != null && !updateRequest.age()
                                                         .equals(customer.getAge())) {
            customer.setAge(updateRequest.age());
            changes = true;
        }

        if (updateRequest.phone() != null && !updateRequest.phone()
                                                           .equals(customer.getPhone())) {
            customer.setPhone(updateRequest.phone());
            changes = true;
        }

        if (!changes) {
            throw new RequestValidationException("no data changes found");
        }

        customerDao.updateCustomer(customer);

//        // Update only the non-null or non-default fields
//        Optional.ofNullable(updateRequest.name()).ifPresent(customer::setName);
//        Optional.ofNullable(updateRequest.email()).ifPresent(customer::setEmail);
//        customer.setPhone(updateRequest.phone() != 0 ? updateRequest.phone() : customer.getPhone());
//        customer.setAge(updateRequest.age() != 0 ? updateRequest.age() : customer.getAge());

    }
}
