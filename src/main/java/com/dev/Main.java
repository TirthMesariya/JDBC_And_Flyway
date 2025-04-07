package com.dev;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Main {
    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);

    }

//    @Bean
//    CommandLineRunner runner (CustomerRepository customerRepository) {
//     return args -> {
//         Customer het = new Customer(
//                 "Het",
//                 "het@gmail.com",
//                 123456789,
//                 20
//         );
//         Customer jay = new Customer(
//                 "Jay",
//                 "jay@gmail.com",
//                 987654321,
//                 25
//         );
//
//         List<Customer> customers = List.of(het, jay);
//         customerRepository.saveAll(customers);
//
//        };
//    }
}
