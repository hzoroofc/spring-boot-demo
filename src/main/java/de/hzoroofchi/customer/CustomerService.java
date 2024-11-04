package de.hzoroofchi.customer;

import de.hzoroofchi.exception.DuplicateResourceException;
import de.hzoroofchi.exception.RequestValidationException;
import de.hzoroofchi.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CustomerService {
    private final CustomerDao customerDao;

    public CustomerService(@Qualifier("jdbc") CustomerDao customerDao) {
        this.customerDao = customerDao;
    }

    public List<Customer> selectAllCustomers() {
        return customerDao.selectAllCustomers();
    }


    public Customer getCustomer(Integer customerId) {
        return
                customerDao.selectCustomerByID(customerId)
                        .orElseThrow(() -> new ResourceNotFoundException("Customer %s not found!".formatted(customerId))
        );
    }

    public Optional<Customer> selectCustomerById(Integer customerId) {
        return Optional.ofNullable(
                customerDao.selectCustomerByID(customerId)
                        .orElseThrow(() -> new ResourceNotFoundException("Customer %s not found!".formatted(customerId)))
        );
    }

    public void deleteCustomerById(Integer customerId) {
        if(!customerDao.existPersonWithId(customerId)){
            throw new ResourceNotFoundException("Customer %s not found!".formatted(customerId));
        }

        customerDao.deleteCustomer(customerId);
    }

    public void addCustomer(CustomerRegistretionRequest customerRegistretionRequest) {
        //if email exists
        if (customerDao.existsPersonWithEmail(customerRegistretionRequest.email())) {
            throw new DuplicateResourceException("Customer already exists!");
        }

        Customer customer = new Customer(customerRegistretionRequest.name(),
                                         customerRegistretionRequest.email(),
                                         customerRegistretionRequest.adress(),
                                         customerRegistretionRequest.age());
        customerDao.inserCustomer(customer);
    }



    public void updateCustomer(Integer customerId,
                               CustomerUpdateRequest updateRequest) {

        Customer customer = getCustomer(customerId);
        boolean changes = false;

        if (!(updateRequest.name() == null) && !updateRequest.name().equals(customer.name)) {
            customer.setName(updateRequest.name());
            changes = true;
        }

        if (!(updateRequest.address() == null) && !updateRequest.address().equals(customer.address)) {
            customer.setAddress(updateRequest.address());
            changes = true;
        }

        if (!(updateRequest.email() == null) && !updateRequest.email().equals(customer.email)) {
            customer.setEmail(updateRequest.email());
            changes = true;
        }

        if (!(updateRequest.age() == null) && !updateRequest.age().equals(customer.age)) {
            customer.setAge(updateRequest.age());
            changes = true;
        }

        if (!changes) {
            throw new RequestValidationException("Nothing to be changed!");
        }

        customerDao.updateCustomer(customer);
    }
}
