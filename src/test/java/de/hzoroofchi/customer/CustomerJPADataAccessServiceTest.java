package de.hzoroofchi.customer;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;

class CustomerJPADataAccessServiceTest {

    private CustomerJPADataAccessService underTest;
    private AutoCloseable autoCloseable;
    @Mock
    private CustomerRepository customerRepository;

    @BeforeEach
    void setUp() {
        autoCloseable = MockitoAnnotations.openMocks(this);
        underTest = new CustomerJPADataAccessService(customerRepository);
    }

    @AfterEach
    void tearDown() throws Exception {
        autoCloseable.close();
    }

    @Test
    void selectAllCustomers() {
        underTest.selectAllCustomers();

        verify(customerRepository)
                .findAll();
    }

    @Test
    void selectCustomerByID() {
        int id = 1;
        underTest.selectCustomerByID(id);

        verify(customerRepository)
                .findById(id);

    }

    @Test
    void deleteCustomer() {
        //Given
        int id = 1;
        //When
        underTest.deleteCustomer(id);
        //Then
        verify(customerRepository).deleteById(id);

    }

    @Test
    void inserCustomer() {
        //Giver
        Customer customer = new Customer("", "", "", 1);
        //When
        underTest.inserCustomer(customer);
        //Then
        verify(customerRepository).save(customer);

    }

    @Test
    void existsPersonWithEmail() {
        String email = "h@gmail.com";
        //Giver
        //When
        underTest.existsPersonWithEmail(email);

        //Then
        verify(customerRepository)
                .existsCustomerByEmail(email);

    }

    @Test
    void existPersonWithId() {
        //Giver
        //When

        //Then


    }

    @Test
    void updateCustomer() {
        //Giver
        //When

        //Then


    }
}