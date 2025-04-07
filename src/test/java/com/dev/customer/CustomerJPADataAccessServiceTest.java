package com.dev.customer;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;

class CustomerJPADataAccessServiceTest {

    private CustomerJPADataAccessService underTest;
    private AutoCloseable autoCloseable;

    @Mock
    private CustomerRepository customerRepository;

    @BeforeEach
    void setUp() {
        autoCloseable = MockitoAnnotations.openMocks(this);
        underTest = new CustomerJPADataAccessService(customerRepository);
    }

    @AfterEach
    void tearDown() throws Exception {
        autoCloseable.close();
    }

    @Test
    void insertCustomers() {
        Customer customer = new Customer(121,"dev","dev@gmail.com",123456789,25);

        underTest.insertCustomers(customer);

        verify(customerRepository)
                .save(customer);

    }

    @Test
    void getAllCustomers() {

        underTest.getAllCustomers();

        verify(customerRepository)
                .findAll();

    }

    @Test
    void getCustomerById() {
        int id = 1;

        underTest.getCustomerById(id);

        verify(customerRepository)
                .findById(id);
    }

    @Test
    void existsPersonWithEmail() {
        String email = "dev@gmail.com";

        underTest.existsPersonWithEmail(email);

        verify(customerRepository)
                .existsCustomerByEmail(email);
    }

    @Test
    void existsPersonWithId() {
        int id = 1;

        underTest.existsPersonWithId(id);

        verify(customerRepository)
                .existsCustomerById(id);
    }

    @Test
    void updateCustomer() {
        Customer customer = new Customer(131,"dev","dev@gmail.com",123456789,25);

        underTest.updateCustomer(customer);

        verify(customerRepository)
                .save(customer);
    }

    @Test
    void deleteCustomerById() {

        int id = 1;

        underTest.deleteCustomerById(id);

        verify(customerRepository)
                .deleteById(id);

    }
}