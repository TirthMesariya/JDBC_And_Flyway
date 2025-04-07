package com.dev.customer;

import java.util.List;
import java.util.Optional;

public interface CustomerDao {
    void insertCustomers(Customer customers);
    List<Customer> getAllCustomers();
    Optional<Customer> getCustomerById(Integer id);
    boolean existsPersonWithEmail(String email);
    boolean existsPersonWithId(Integer customerId);
    void updateCustomer(Customer customer);
    void deleteCustomerById(Integer id);




}
