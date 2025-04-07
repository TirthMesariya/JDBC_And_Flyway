package com.dev.customer;

import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository("list")
public class CustomerListDataAccessService implements CustomerDao {


    //    db
    private static final List<Customer> customers;

    static {
        customers = new ArrayList<>();
        Customer het = new Customer(
                101,
                "Het",
                "het@gmail.com",
                123456789,
                20
        );
        customers.add(het);

        Customer jay = new Customer(
                102,
                "Jay",
                "jay@gmail.com",
                987654321,
                25
        );
        customers.add(jay);
    }


//    public CustomerDataAccessService(CustomerDao customerDao) {
//        this.customerDao = customerDao;
//    }
    public Optional<Customer> getCustomerById(Integer id){
        return customers.stream()
                .filter(c -> c.getId().equals(id))
                .findFirst();


    }

    public void deleteCustomerById(Integer id){
        customers.stream()
                .filter(c -> c.getId().equals(id))
                .findFirst()
                .ifPresent(customers::remove);
    }
    public void updateCustomer(Customer customer){
//        customerDao.updateCustomer(customer);
    }
    public List<Customer> getAllCustomers(){
        return customers;
    }

    @Override
    public void insertCustomers(Customer customer) {
        customers.add(customer);
    }

    @Override
    public boolean existsPersonWithEmail(String email) {
        return customers.stream().anyMatch(c -> c.getEmail().equals(email));
    }

    @Override
    public boolean existsPersonWithId(Integer customerId) {
        return customers.stream().anyMatch(c -> c.getId().equals(customerId));
    }
}
