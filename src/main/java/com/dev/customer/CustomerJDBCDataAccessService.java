package com.dev.customer;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository("jdbc")
public class CustomerJDBCDataAccessService implements CustomerDao{

    private final JdbcTemplate jdbcTemplate;
    private final CustomerRowMapper customerRowMapper;

    public CustomerJDBCDataAccessService(JdbcTemplate jdbcTemplate, CustomerRowMapper customerRowMapper) {
        this.jdbcTemplate = jdbcTemplate;
        this.customerRowMapper = customerRowMapper;
    }

    @Override
    public void insertCustomers(Customer customers) {
        var sql = """
                INSERT INTO customer (name, email, phone, age)
                VALUES (?,?,?,?)
                """;
        int result = jdbcTemplate.update(
                sql,
                customers.getName(),
                customers.getEmail(),
                customers.getPhone(),
                customers.getAge()
        );

        System.out.println("jdbcTemplate.update = " + result);


    }

    @Override
    public List<Customer> getAllCustomers() {
        var sql = """
                SELECT id, name, email, phone, age
                FROM customer
                """;

//        RowMapper<Customer> customerRowMapper = (rs, rowNum) -> {
//            Customer customer = new Customer(
//                    rs.getInt("id"),
//                    rs.getString("name"),
//                    rs.getString("email"),
//                    rs.getInt("phone"),
//                    rs.getInt("age")
//            );
//            return customer;
//        };
//        List<Customer> customers = jdbcTemplate.query(sql, customerRowMapper);
//        return customers;

        return  jdbcTemplate.query(sql,customerRowMapper);
    }

    @Override
    public Optional<Customer> getCustomerById(Integer id) {
        var sql = """
                SELECT id, name, email, phone, age
                FROM customer
                WHERE id = ?
                """;
       return jdbcTemplate.query(sql,customerRowMapper,id)
                .stream()
                .findFirst();
    }

    @Override
    public boolean existsPersonWithEmail(String email) {
        var sql = """
                SELECT count(id)
                FROM customer
                WHERE email = ?
                """;
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, email);
        return count != null && count > 0;
    }

    @Override
    public boolean existsPersonWithId(Integer customerId) {
        var sql = """
                SELECT count(id)
                FROM customer
                WHERE id = ?
                """;
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, customerId);
        return count != null && count > 0;

    }

    @Override
    public void updateCustomer(Customer customer) {
        if (customer.getName() != null){
            String sql = "UPDATE customer SET name = ? WHERE id = ?";
            int result = jdbcTemplate.update(
                    sql,
                    customer.getName(),
                    customer.getId()
            );
            System.out.println("update customer name result = " + result);
        }
        if (customer.getName() != null){
            String sql = "UPDATE customer SET email = ? WHERE id = ?";
            int result = jdbcTemplate.update(
                    sql,
                    customer.getEmail(),
                    customer.getId()
            );
            System.out.println("update customer email result = " + result);
        }
        if (customer.getName() != null){
            String sql = "UPDATE customer SET phone = ? WHERE id = ?";
            int result = jdbcTemplate.update(
                    sql,
                    customer.getPhone(),
                    customer.getId()
            );
            System.out.println("update customer phone result = " + result);
        }
        if (customer.getName() != null){
            String sql = "UPDATE customer SET age = ? WHERE id = ?";
            int result = jdbcTemplate.update(
                    sql,
                    customer.getAge(),
                    customer.getId()
            );
            System.out.println("update customer age result = " + result);
        }
    }

    @Override
    public void deleteCustomerById(Integer id) {
        var sql = """
                DELETE FROM customer
                WHERE id = ?
                """;
        int result = jdbcTemplate.update(sql, id);
        System.out.println("deleteCustomerById result = " + result);
    }
}
