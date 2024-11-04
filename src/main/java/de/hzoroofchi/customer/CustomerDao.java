package de.hzoroofchi.customer;

import java.util.List;
import java.util.Optional;

public interface CustomerDao {
    List<Customer> selectAllCustomers();
    Optional<Customer> selectCustomerByID(int customerId);
    void deleteCustomer(Integer customerId);
    void inserCustomer(Customer customer);
    boolean existsPersonWithEmail(String email);
    boolean existPersonWithId(Integer customerId);
    void updateCustomer(Customer customer);
}
