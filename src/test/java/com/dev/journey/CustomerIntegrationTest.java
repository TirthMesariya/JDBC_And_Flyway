package com.dev.journey;

import com.dev.customer.Customer;
import com.dev.customer.CustomerRegistrationRequest;
import com.dev.customer.CustomerUpdateRequest;
import com.github.javafaker.Faker;
import com.github.javafaker.Name;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Random;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@SpringBootTest(webEnvironment = RANDOM_PORT)
public class CustomerIntegrationTest {

    @Autowired
    private WebTestClient webTestClient;

    private static final Random RANDOM = new Random();
    private static final String CUSTOMER_URI = "/api/v1/customers";

    @Test
    void canRegisterCustomer() {
        // create registration request
        Faker faker = new Faker();
        Name fakerName = faker.name();
        String name = fakerName.fullName();
        String email = fakerName.lastName() + UUID.randomUUID() + "@devcode.com";
        int age = RANDOM.nextInt(
                1,
                100
        );
        int phone = RANDOM.nextInt(99999999);
        CustomerRegistrationRequest request = new CustomerRegistrationRequest(
                name,
                email,
                phone,
                age
        );
        // send post request
        webTestClient.post()
                     .uri(CUSTOMER_URI)
                     .accept(MediaType.APPLICATION_JSON)
                     .contentType(MediaType.APPLICATION_JSON)
                     .body(
                             Mono.just(request),
                             CustomerRegistrationRequest.class
                     )
                     .exchange()
                     .expectStatus()
                     .isOk();

        //get all customers
        List<Customer> allCustomer = webTestClient.get()
                                                  .uri(CUSTOMER_URI)
                                                  .accept(MediaType.APPLICATION_JSON)
                                                  .exchange()
                                                  .expectStatus()
                                                  .isOk()
                                                  .expectBodyList(new ParameterizedTypeReference<Customer>() {
                                                  })
                                                  .returnResult()
                                                  .getResponseBody();

        // make sure that customer is present
        Customer expectedCustomer = new Customer(
                name,
                email,
                phone,
                age
        );

        assertThat(allCustomer).usingRecursiveFieldByFieldElementComparatorIgnoringFields("id")
                               .contains(expectedCustomer);

        //getCustomerById

        int id = allCustomer.stream()
                            .filter(c -> c.getEmail()
                                          .equals(email))
                            .map(Customer::getId)
                            .findFirst()
                            .orElseThrow();

        expectedCustomer.setId(id);

        webTestClient.get()
                     .uri(
                             CUSTOMER_URI + "/{id}",
                             id
                     )
                     .accept(MediaType.APPLICATION_JSON)
                     .exchange()
                     .expectStatus()
                     .isOk()
                     .expectBody(new ParameterizedTypeReference<Customer>() {
                     })
                     .isEqualTo(expectedCustomer);
    }

    @Test
    void canDeleteCustomer() {
        // create registration request
        Faker faker = new Faker();
        Name fakerName = faker.name();
        String name = fakerName.fullName();
        String email = fakerName.lastName() + UUID.randomUUID() + "@devcode.com";
        int age = RANDOM.nextInt(
                1,
                100
        );
        int phone = RANDOM.nextInt(99999999);
        CustomerRegistrationRequest request = new CustomerRegistrationRequest(
                name,
                email,
                phone,
                age
        );
        // send post request
        webTestClient.post()
                     .uri(CUSTOMER_URI)
                     .accept(MediaType.APPLICATION_JSON)
                     .contentType(MediaType.APPLICATION_JSON)
                     .body(
                             Mono.just(request),
                             CustomerRegistrationRequest.class
                     )
                     .exchange()
                     .expectStatus()
                     .isOk();

        //get all customers
        List<Customer> allCustomer = webTestClient.get()
                                                  .uri(CUSTOMER_URI)
                                                  .accept(MediaType.APPLICATION_JSON)
                                                  .exchange()
                                                  .expectStatus()
                                                  .isOk()
                                                  .expectBodyList(new ParameterizedTypeReference<Customer>() {
                                                  })
                                                  .returnResult()
                                                  .getResponseBody();

        //getCustomerById
        int id = allCustomer.stream()
                            .filter(c -> c.getEmail()
                                          .equals(email))
                            .map(Customer::getId)
                            .findFirst()
                            .orElseThrow();

        // delete customer
        webTestClient.delete()
                     .uri(
                             CUSTOMER_URI + "/{id}",
                             id
                     )
                     .accept(MediaType.APPLICATION_JSON)
                     .exchange()
                     .expectStatus()
                     .isOk();

        webTestClient.get()
                     .uri(
                             CUSTOMER_URI + "/{id}",
                             id
                     )
                     .accept(MediaType.APPLICATION_JSON)
                     .exchange()
                     .expectStatus()
                     .isNotFound();
    }

    @Test
    void canUpdateCustomer() {

        // create registration request
        Faker faker = new Faker();
        Name fakerName = faker.name();
        String name = fakerName.fullName();
        String email = fakerName.lastName() + UUID.randomUUID() + "@devcode.com";
        int age = RANDOM.nextInt(
                1,
                100
        );
        int phone = RANDOM.nextInt(99999999);
        CustomerRegistrationRequest request = new CustomerRegistrationRequest(
                name,
                email,
                phone,
                age
        );
        // send post request
        webTestClient.post()
                     .uri(CUSTOMER_URI)
                     .accept(MediaType.APPLICATION_JSON)
                     .contentType(MediaType.APPLICATION_JSON)
                     .body(
                             Mono.just(request),
                             CustomerRegistrationRequest.class
                     )
                     .exchange()
                     .expectStatus()
                     .isOk();

        //get all customers
        List<Customer> allCustomer = webTestClient.get()
                                                  .uri(CUSTOMER_URI)
                                                  .accept(MediaType.APPLICATION_JSON)
                                                  .exchange()
                                                  .expectStatus()
                                                  .isOk()
                                                  .expectBodyList(new ParameterizedTypeReference<Customer>() {
                                                  })
                                                  .returnResult()
                                                  .getResponseBody();

        //getCustomerById
        int id = allCustomer.stream()
                            .filter(c -> c.getEmail()
                                          .equals(email))
                            .map(Customer::getId)
                            .findFirst()
                            .orElseThrow();

        String newName = "Anand";
        CustomerUpdateRequest customerUpdateRequest = new CustomerUpdateRequest(
                newName,
                null,
                null,
                null
        );

        // update customer
        webTestClient.put()
                     .uri(
                             CUSTOMER_URI + "/{id}",
                             id
                     )
                     .accept(MediaType.APPLICATION_JSON)
                     .contentType(MediaType.APPLICATION_JSON)
                     .body(
                             Mono.just(customerUpdateRequest),
                             CustomerUpdateRequest.class
                     )
                     .exchange()
                     .expectStatus()
                     .isOk();

        //get customer by id
        Customer updateCustomer = webTestClient.get()
                                             .uri(
                                                     CUSTOMER_URI + "/{id}",
                                                     id
                                             )
                                             .accept(MediaType.APPLICATION_JSON)
                                             .exchange()
                                             .expectStatus()
                                             .isOk()
                                             .expectBody(Customer.class)
                                             .returnResult()
                                             .getResponseBody();

        Customer expected = new Customer(
                id,newName,email,phone,age
        );

        assertThat(updateCustomer).isEqualTo(expected);
    }
}
