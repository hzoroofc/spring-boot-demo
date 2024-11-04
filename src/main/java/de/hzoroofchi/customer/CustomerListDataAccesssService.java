package de.hzoroofchi.customer;

import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository("list")
public class CustomerListDataAccesssService implements CustomerDao{

    static List<Customer> customers = new ArrayList<>();

    static {
        customers.add(new Customer("Jamila", "jamila@web.de", "Rechterallee 28", 28, 1));
        customers.add(new Customer("Ali", "alite@gmx.de", "Bürger Straße 33", 31, 2));
        customers.add(new Customer("Leonardo", "leo@web.de", "Am Aker 1", 20 ,3 ));
    }

    @Override
    public List<Customer> selectAllCustomers() {
        return customers;
    }

    @Override
    public Optional<Customer> selectCustomerByID(int customerId) {
        return customers.stream()
                .filter(c -> c.getId() == customerId)
                .findFirst();
    }

    @Override
    public void deleteCustomer(Integer customerId) {

    }

    @Override
    public void inserCustomer(Customer customer) {
        customers.add(customer);
    }

    @Override
    public boolean existsPersonWithEmail(String email) {
        return customers
                .stream()
                .anyMatch(
                        e -> e.getEmail()
                                .equals(email)
                );
    }

    @Override
    public boolean existPersonWithId(Integer customerId) {
        return false;
    }

    @Override
    public void updateCustomer(Customer customer) {

    }

}
