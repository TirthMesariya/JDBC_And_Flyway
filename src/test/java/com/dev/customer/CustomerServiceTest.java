package com.dev.customer;

import com.dev.exception.DuplicateResourceException;
import com.dev.exception.RequestValidationException;
import com.dev.exception.ResourceNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class CustomerServiceTest {

    @Mock
    private CustomerDao customerDao;
    private CustomerService underTest;

    @BeforeEach
    void setUp() {
        underTest = new CustomerService(customerDao);
    }

    @Test
    void getAllCustomers() {

        underTest.getAllCustomers();

        verify(customerDao).getAllCustomers();
    }

    @Test
    void getCustomerById() {
        int id = 1;
        Customer customer = new Customer(
                id,
                "dev",
                "dev2@gmail.com",
                123456789,
                25
        );
        when(customerDao.getCustomerById(id)).thenReturn(Optional.of(customer));

        var actual = underTest.getCustomerById(id);

        assertThat(actual).isEqualTo(customer);

    }

    @Test
    void willThrowWhenGetCustomerReturnEmptyOptional() {
        int id = 1;

        when(customerDao.getCustomerById(id)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> underTest.getCustomerById(id)).isInstanceOf(ResourceNotFoundException.class)
                                                               .hasMessage("customer with id [%d] not found".formatted(id));

    }

    @Test
    void addCustomer() {
        int id = 1;
        String email = "dev2@gmail.com";

        when(customerDao.existsPersonWithEmail(email)).thenReturn(false);

        CustomerRegistrationRequest request = new CustomerRegistrationRequest(
                "dev",
                email,
                123456789,
                25
        );

        underTest.addCustomer(request);

        ArgumentCaptor<Customer> customerArgumentCaptor = ArgumentCaptor.forClass(Customer.class);

        verify(customerDao).insertCustomers(customerArgumentCaptor.capture());

        Customer customerArgumentCaptorValue = customerArgumentCaptor.getValue();

        assertThat(customerArgumentCaptorValue.getId()).isNull();
        assertThat(customerArgumentCaptorValue.getName()).isEqualTo(request.name());
        assertThat(customerArgumentCaptorValue.getEmail()).isEqualTo(request.email());
        assertThat(customerArgumentCaptorValue.getPhone()).isEqualTo(request.phone());
        assertThat(customerArgumentCaptorValue.getAge()).isEqualTo(request.age());


    }


    @Test
    void willThrowWhenEmailExistsWhileAddingACustomer() {

        String email = "dev2@gmail.com";

        when(customerDao.existsPersonWithEmail(email)).thenReturn(true);

        CustomerRegistrationRequest request = new CustomerRegistrationRequest(
                "dev",
                email,
                123456789,
                25
        );

        assertThatThrownBy(() -> underTest.addCustomer(request)).isInstanceOf(DuplicateResourceException.class)
                                                                .hasMessage("email already exists");


        verify(
                customerDao,
                never()
        ).insertCustomers(any());

    }

    @Test
    void doesCustomerExistWithEmail() {
    }

    @Test
    void doesCustomerExistWithId() {
    }

    @Test
    void deleteCustomerById() {
        int id = 1;

        when(customerDao.existsPersonWithId(id)).thenReturn(true);

        underTest.deleteCustomerById(id);

        verify(customerDao).deleteCustomerById(id);
    }

    @Test
    void willThrowDeleteCustomerByIdNotExists() {
        int id = 1;

        when(customerDao.existsPersonWithId(id)).thenReturn(false);

        assertThatThrownBy(() -> underTest.deleteCustomerById(id)).isInstanceOf(ResourceNotFoundException.class)
                                                                  .hasMessage("customer with id [%d] not found".formatted(id));

        verify(
                customerDao,
                never()
        ).deleteCustomerById(id);
    }

    @Test
    void updateCustomer() {
        int id = 1;
        Customer customer = new Customer(
                id,
                "dev",
                "dev2@gmail.com",
                123456789,
                25
        );

        when(customerDao.getCustomerById(id)).thenReturn(Optional.of(customer));

        CustomerUpdateRequest jay = new CustomerUpdateRequest(
                "jay",
                "jay@gmail.com",
                213456789,
                24
        );

        when(customerDao.existsPersonWithEmail("jay@gmail.com")).thenReturn(false);

        underTest.updateCustomer(
                id,
                jay
        );

        ArgumentCaptor<Customer> customerArgumentCaptor = ArgumentCaptor.forClass(Customer.class);
        verify(customerDao).updateCustomer(customerArgumentCaptor.capture());

        Customer captorValue = customerArgumentCaptor.getValue();

        assertThat(captorValue.getName()).isEqualTo(jay.name());
        assertThat(captorValue.getEmail()).isEqualTo(jay.email());
        assertThat(captorValue.getPhone()).isEqualTo(jay.phone());
        assertThat(captorValue.getAge()).isEqualTo(jay.age());


    }

    @Test
    void canUpdateOnlyCustomerName() {
        int id = 1;
        Customer customer = new Customer(
                id,
                "dev",
                "dev2@gmail.com",
                123456789,
                25
        );

        when(customerDao.getCustomerById(id)).thenReturn(Optional.of(customer));

        CustomerUpdateRequest jay = new CustomerUpdateRequest(
                "kiran",
                null,
                null,
                null
        );


        underTest.updateCustomer(
                id,
                jay
        );

        ArgumentCaptor<Customer> customerArgumentCaptor = ArgumentCaptor.forClass(Customer.class);
        verify(customerDao).updateCustomer(customerArgumentCaptor.capture());

        Customer captorValue = customerArgumentCaptor.getValue();

        assertThat(captorValue.getName()).isEqualTo(jay.name());
        assertThat(captorValue.getEmail()).isEqualTo(customer.getEmail());
        assertThat(captorValue.getPhone()).isEqualTo(customer.getPhone());
        assertThat(captorValue.getAge()).isEqualTo(customer.getAge());
    }

    @Test
    void canUpdateOnlyCustomerEmail() {
        int id = 1;
        Customer customer = new Customer(
                id,
                "dev",
                "dev2@gmail.com",
                123456789,
                25
        );

        when(customerDao.getCustomerById(id)).thenReturn(Optional.of(customer));

        CustomerUpdateRequest jay = new CustomerUpdateRequest(
                null,
                "kiran@gmail.com",
                null,
                null
        );

        when(customerDao.existsPersonWithEmail("kiran@gmail.com")).thenReturn(false);


        underTest.updateCustomer(
                id,
                jay
        );

        ArgumentCaptor<Customer> customerArgumentCaptor = ArgumentCaptor.forClass(Customer.class);
        verify(customerDao).updateCustomer(customerArgumentCaptor.capture());

        Customer captorValue = customerArgumentCaptor.getValue();

        assertThat(captorValue.getName()).isEqualTo(customer.getName());
        assertThat(captorValue.getEmail()).isEqualTo(jay.email());
        assertThat(captorValue.getPhone()).isEqualTo(customer.getPhone());
        assertThat(captorValue.getAge()).isEqualTo(customer.getAge());
    }

    @Test
    void canUpdateOnlyCustomerPhone() {
        int id = 1;
        Customer customer = new Customer(
                id,
                "dev",
                "dev2@gmail.com",
                123456789,
                25
        );

        when(customerDao.getCustomerById(id)).thenReturn(Optional.of(customer));

        CustomerUpdateRequest jay = new CustomerUpdateRequest(
                null,
                null,
                321456789,
                null
        );

        underTest.updateCustomer(
                id,
                jay
        );

        ArgumentCaptor<Customer> customerArgumentCaptor = ArgumentCaptor.forClass(Customer.class);
        verify(customerDao).updateCustomer(customerArgumentCaptor.capture());

        Customer captorValue = customerArgumentCaptor.getValue();

        assertThat(captorValue.getName()).isEqualTo(customer.getName());
        assertThat(captorValue.getEmail()).isEqualTo(customer.getEmail());
        assertThat(captorValue.getPhone()).isEqualTo(jay.phone());
        assertThat(captorValue.getAge()).isEqualTo(customer.getAge());
    }

    @Test
    void canUpdateOnlyCustomerAge() {
        int id = 1;
        Customer customer = new Customer(
                id,
                "dev",
                "dev2@gmail.com",
                123456789,
                25
        );

        when(customerDao.getCustomerById(id)).thenReturn(Optional.of(customer));

        CustomerUpdateRequest jay = new CustomerUpdateRequest(
                null,
                null,
                null,
                24
        );


        underTest.updateCustomer(
                id,
                jay
        );

        ArgumentCaptor<Customer> customerArgumentCaptor = ArgumentCaptor.forClass(Customer.class);
        verify(customerDao).updateCustomer(customerArgumentCaptor.capture());

        Customer captorValue = customerArgumentCaptor.getValue();

        assertThat(captorValue.getName()).isEqualTo(customer.getName());
        assertThat(captorValue.getEmail()).isEqualTo(customer.getEmail());
        assertThat(captorValue.getPhone()).isEqualTo(customer.getPhone());
        assertThat(captorValue.getAge()).isEqualTo(jay.age());
    }

    @Test
    void willThrowWhenTryingToUpdateCustomerEmailWhenAlreadyTaken() {
        int id = 1;
        Customer customer = new Customer(
                id,
                "dev",
                "dev2@gmail.com",
                123456789,
                25
        );

        when(customerDao.getCustomerById(id)).thenReturn(Optional.of(customer));

        CustomerUpdateRequest jay = new CustomerUpdateRequest(
                null,
                "Kiran@gmail.com",
                null,
                null
        );

        when(customerDao.existsPersonWithEmail("Kiran@gmail.com")).thenReturn(true);

        assertThatThrownBy(() -> underTest.updateCustomer(
                id,
                jay
        )).isInstanceOf(DuplicateResourceException.class)
          .hasMessage("email already exists");

        verify(
                customerDao,
                never()
        ).updateCustomer(any());
    }

    @Test
    void willThrowWhenCustomerUpdateHasNoChanges() {
        int id = 1;
        Customer customer = new Customer(
                id,
                "dev",
                "dev2@gmail.com",
                123456789,
                25
        );

        when(customerDao.getCustomerById(id)).thenReturn(Optional.of(customer));

        CustomerUpdateRequest jay = new CustomerUpdateRequest(
                customer.getName(),
                customer.getEmail(),
                customer.getPhone(),
                customer.getAge()
        );

        assertThatThrownBy(() -> underTest.updateCustomer(
                id,
                jay
        )).isInstanceOf(RequestValidationException.class)
          .hasMessage("no data changes found");

        verify(
                customerDao,
                never()
        ).updateCustomer(any());
    }


}