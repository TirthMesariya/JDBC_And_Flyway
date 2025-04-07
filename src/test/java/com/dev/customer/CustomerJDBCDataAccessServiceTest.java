package com.dev.customer;

import com.dev.AbstractTestcontainers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class CustomerJDBCDataAccessServiceTest extends AbstractTestcontainers {

    private CustomerJDBCDataAccessService underTest;
    private final CustomerRowMapper customerRowMapper = new CustomerRowMapper();

    @BeforeEach
    void setUp() {
        underTest = new CustomerJDBCDataAccessService(
                getJdbcTemplate(),
                customerRowMapper
        );
    }

//    @Test
//    void insertCustomers() {
//        String email = FAKER.internet().safeEmailAddress() + "-" + UUID.randomUUID();
//        Customer customer = new Customer(
//                FAKER.name().fullName(),
//                email,
//                432145678,
//                25
//        );
//
//        underTest.insertCustomers(customer);
//        System.out.println("Inserted customer id: "+ customer.getId());
//        // Debug: Print the inserted customer's email
//        System.out.println("Inserted customer email: " + customer.getEmail());
//
//        // Retrieve the customer by ID
//        Optional<Customer> actual = underTest.getCustomerById(customer.getId());
//
//        // Debug: Print whether the customer was found
//        System.out.println("Customer found: " + actual.isPresent());
//
//        assertThat(actual).isPresent().hasValueSatisfying(c -> {
//            assertThat(c.getId()).isEqualTo(customer.getId());
//            assertThat(c.getName()).isEqualTo(customer.getName());
//            assertThat(c.getEmail()).isEqualTo(customer.getEmail());
//            assertThat(c.getPhone()).isEqualTo(customer.getPhone());
//            assertThat(c.getAge()).isEqualTo(customer.getAge());
//        });
//    }

    @Test
    void getAllCustomers() {
        Customer customer = new Customer(
                FAKER.name().firstName(),
                FAKER.internet().safeEmailAddress() + "-" + UUID.randomUUID(),
                123456789,
                25
        );

        underTest.insertCustomers(customer);

        List<Customer> actual = underTest.getAllCustomers();

        assertThat(actual).isNotEmpty();
    }

    @Test
    void getCustomerById() {
        String email = FAKER.internet().safeEmailAddress() + "-" + UUID.randomUUID();
        Customer customer = new Customer(
                FAKER.name().firstName(),
                email,
                123456789,
                25
        );

        underTest.insertCustomers(customer);

        int id = underTest.getAllCustomers()
                .stream()
                .filter(c -> c.getEmail().equals(email))
                .map(Customer::getId)
                .findFirst()
                .orElseThrow();

        Optional<Customer> actual = underTest.getCustomerById(id);

        assertThat(actual).isPresent().hasValueSatisfying(c -> {
                assertThat(c.getId()).isEqualTo(id);
                assertThat(c.getName()).isEqualTo(customer.getName());
                assertThat(c.getEmail()).isEqualTo(customer.getEmail());
                assertThat(c.getPhone()).isEqualTo(customer.getPhone());
                assertThat(c.getAge()).isEqualTo(customer.getAge());

                } );
    }

    @Test
    void willReturnEmptyWhenGetCustomerById(){
        int id = 0;

       var actual = underTest.getCustomerById(id);


        assertThat(actual).isEmpty();
    }

    @Test
    void existsPersonWithEmail() {
        String email = FAKER.internet().safeEmailAddress() + "-" + UUID.randomUUID();
        Customer customer = new Customer(
                FAKER.name().firstName(),
                email,
                123456789,
                25
        );

        underTest.insertCustomers(customer);

        boolean actual = underTest.existsPersonWithEmail(email);
        assertThat(actual).isTrue();
    }

    @Test
    void existsPersonWithEmailReturnsFalseWhenDoesNotExists(){
        String email = FAKER.internet().safeEmailAddress() + "-"+ UUID.randomUUID();

        boolean actual = underTest.existsPersonWithEmail(email);

        assertThat(actual).isFalse();
    }

    @Test
    void existsPersonWithId() {

        String email = FAKER.internet().safeEmailAddress()+ "-" + UUID.randomUUID();
        Customer customer = new Customer(
                FAKER.name().firstName(),
                email,
                123456789,
                25
        );

        underTest.insertCustomers(customer);

        int id = underTest.getAllCustomers()
                .stream()
                .filter(c -> c.getEmail().equals(email))
                .map(Customer::getId)
                .findFirst()
                .orElseThrow();

        var actual = underTest.existsPersonWithId(id);

        assertThat(actual).isTrue();

//        Customer customer = new Customer(
//                FAKER.name().firstName(),
//                FAKER.internet().safeEmailAddress() + "-" + UUID.randomUUID(),
//                123456789,
//                25
//        );
//
//        underTest.insertCustomers(customer);
//
//        boolean actual = underTest.existsPersonWithId(customer.getId());
//        assertThat(actual).isTrue();
    }

    @Test
    void updateCustomer() {

        String email = FAKER.internet().safeEmailAddress()+ "-" + UUID.randomUUID();
        System.out.println(email);
        Customer customer = new Customer(
                FAKER.name().fullName(),
                email,
                123456789,
                25
        );

        underTest.insertCustomers(customer);

        int id = underTest.getAllCustomers()
                .stream()
                .filter(c -> c.getEmail().equals(email))
                .map(Customer::getId)
                .findFirst()
                .orElseThrow();

        var newName = "com/dev";

        Customer update = new Customer();
        update.setId(id);
        update.setName("newName");
        update.setEmail(UUID.randomUUID().toString());
        update.setPhone(543216789);
        update.setAge(24);

        underTest.updateCustomer(update);

        Optional<Customer> actual = underTest.getCustomerById(id);

//        assertThat(actual).isPresent().hasValueSatisfying(c -> {
//            assertThat(c.getId()).isEqualTo(id);
//            assertThat(c.getName()).isEqualTo(newName);
//            assertThat(c.getEmail()).isEqualTo(customer.getEmail());
//            assertThat(c.getPhone()).isEqualTo(customer.getPhone());
//            assertThat(c.getAge()).isEqualTo(customer.getAge());
//        });
        assertThat(actual).isPresent().hasValue(update);

    }

    @Test
    void updateCustomerEmail(){}

    @Test
    void deleteCustomerById() {

        String email = FAKER.internet().safeEmailAddress()+ "-" + UUID.randomUUID();
        Customer customer = new Customer(
                FAKER.name().firstName(),
                email,
                123456789,
                25
        );

        underTest.insertCustomers(customer);

        int id = underTest.getAllCustomers()
                .stream()
                .filter(c -> c.getEmail().equals(email))
                .map(Customer::getId)
                .findFirst()
                .orElseThrow();



             underTest.deleteCustomerById(id);

        Optional<Customer> actual = underTest.getCustomerById(id);

        assertThat(actual).isNotPresent();

    }
}