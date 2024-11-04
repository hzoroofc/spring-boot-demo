package de.hzoroofchi.customer;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository("jdbc")
public class CustomerJDBCDataAccessService implements CustomerDao{

    private final CustomerRowMapper customerRowMapper;
    private final JdbcTemplate jdbcTemplate;

    public CustomerJDBCDataAccessService(CustomerRowMapper customerRowMapper, JdbcTemplate jdbcTemplate) {
        this.customerRowMapper = customerRowMapper;
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Customer> selectAllCustomers() {
        var sql = """
                    SELECT name, email, address, age, id
                    FROM customer
                """;

        return jdbcTemplate.query(sql, customerRowMapper);
    }

    @Override
    public Optional<Customer> selectCustomerByID(int customerId) {
        var sql = """
                    SELECT name, email, address, age, id
                    FROM customer
                    WHERE id = ?
                """;
        return jdbcTemplate.query(sql, customerRowMapper, customerId).stream().findFirst();
    }

    @Override
    public void deleteCustomer(Integer customerId) {
        var sql = """
                    DELETE FROM customer
                    WHERE id = ?
                """;
        jdbcTemplate.update(sql, customerId);
    }

    @Override
    public void inserCustomer(Customer customer) {
        var sql = """
                INSERT INTO customer(name, email, age, address)
                VALUES (?, ?, ?, ?)
                """;
        int result = jdbcTemplate.update(
                sql,
                customer.getName(),
                customer.getEmail(),
                customer.getAge(),
                customer.getAddress()
        );

        System.out.println("jdbcTemplate.update " + result);
    }

    @Override
    public boolean existsPersonWithEmail(String email) {
        var sql = """
                    SELECT count(1)
                    FROM customer
                    WHERE email = ?
                """;
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, email);
        return count != null && count > 0;
    }

    @Override
    public boolean existPersonWithId(Integer customerId) {
        var sql = """
                SELECT count(id) 
                FROM customer
                WHERE id = ?
                """;

        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, customerId);
        return  count != null && count > 0;

    }

    @Override
    public void updateCustomer(Customer customer) {
        if(customer.getName() != null) {
            var sql = """
                    UPDATE customer SET name = ? WHERE id = ?
                    """;
            int result = jdbcTemplate.update(sql, customer.getName(), customer.getId());
            System.out.println("update customer result: " + result);
        }

        if(customer.getAge() != 0) {
            var sql = """
                    UPDATE customer SET age = ? WHERE id = ?
                    """;
            int result = jdbcTemplate.update(sql, customer.getAge(), customer.getId());
            System.out.println("update customer result: " + result);
        }

        if(customer.getEmail() != null) {
            var sql = """
                    UPDATE customer SET email = ? WHERE id = ?
                    """;
            int result = jdbcTemplate.update(sql, customer.getEmail(), customer.getId());
            System.out.println("update customer result: " + result);
        }

        if(customer.getAddress() != null) {
            var sql = """
                    UPDATE customer SET address = ? WHERE id = ?
                    """;
            int result = jdbcTemplate.update(sql, customer.getAddress(), customer.getId());
            System.out.println("update customer result: " + result);
        }
    }
}
