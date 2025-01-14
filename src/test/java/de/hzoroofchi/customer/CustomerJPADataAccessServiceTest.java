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
        //When
        underTest.selectAllCustomers();

        //Then
        verify(customerRepository)
                .findAll();
    }

    @Test
    void selectCustomerByID() {
        int id = 1;
        //When
        underTest.selectCustomerByID(id);
        //Then
        verify(customerRepository)
                .findById(id);

    }

    @Test
    void deleteCustomer() {
        int id = 1;
        //When
        underTest.deleteCustomer(id);
        //Then
        verify(customerRepository).deleteById(id);

    }

    @Test
    void inserCustomer() {
       Customer customer = new Customer("John", "john@gmail.com", 18);
        //When

        //Then

    }

    @Test
    void existsPersonWithEmail() {
        //Giver
        //When

        //Then


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