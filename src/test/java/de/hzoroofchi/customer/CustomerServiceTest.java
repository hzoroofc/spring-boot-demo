package de.hzoroofchi.customer;

import de.hzoroofchi.exception.DuplicateResourceException;
import de.hzoroofchi.exception.RequestValidationException;
import de.hzoroofchi.exception.ResourceNotFoundException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class CustomerServiceTest {

    @Mock
    private CustomerDao customerDao;
    private CustomerService underTest;

    @BeforeEach
    void setUp() {
        underTest = new CustomerService(customerDao);
    }


    @Test
    void selectAllCustomers() {

        underTest.selectAllCustomers();

        verify(customerDao).selectAllCustomers();


    }

    @Test
    void canGetCustomer() {

        //Given
        int id = 10;
        Customer customer = new Customer("Alex", "alex@gmail.com", 28);
        when(customerDao.selectCustomerByID(id)).thenReturn(Optional.of(customer));

        //When
        Customer actual = underTest.getCustomer(10);

        //Then
        assertThat(actual).isEqualTo(customer);

    }

    @Test
    void willThrowWhenGetCustomerReturnEmptyOptional() {
        //Given
        int id = 10;
        when(customerDao.selectCustomerByID(id)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> underTest.selectCustomerById(id))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining(
                        "Customer %s not found!".formatted(id)
                );

    }

    @Test
    void addCustomer() {
        //Given
        String email = "alex@gmail.com";

        when(customerDao.existsPersonWithEmail(email)).thenReturn(false);

        CustomerRegistretionRequest request = new CustomerRegistretionRequest(
                "Alex", email, "Mundsburg 1", 29, 1
        );
        //When
        underTest.addCustomer(request);

        //Then
        ArgumentCaptor<Customer> customerArgumentCaptor = ArgumentCaptor.forClass(Customer.class);

        verify(customerDao).inserCustomer(customerArgumentCaptor.capture());

        Customer captoredCustomer = customerArgumentCaptor.getValue();

//        assertThat(captoredCustomer.getId()).isNull();
        assertThat(captoredCustomer.getName()).isEqualTo(request.name());
        assertThat(captoredCustomer.getAddress()).isEqualTo(request.adress());
        assertThat(captoredCustomer.getEmail()).isEqualTo(request.email());
        assertThat(captoredCustomer.getAge()).isEqualTo(request.age());


    }

    @Test
    void willThrowWhenEmailExistsWhileAddingCustomer() {
        //Given
        String email = "alex@gmail.com";

        when(customerDao.existsPersonWithEmail(email)).thenReturn(true);

        CustomerRegistretionRequest request = new CustomerRegistretionRequest(
                "Alex", email, "Mundsburg 1", 29, 1
        );
        //When
        assertThatThrownBy(() -> underTest.addCustomer(request))
                .isInstanceOf(DuplicateResourceException.class)
                .hasMessage("Customer already exists!");

        //Then

        verify(customerDao, never()).inserCustomer(any());


    }

    @Test
    void willThrowWhenCustomerNotExists() {
        //Given
        int id = 10;

        when(customerDao.existPersonWithId(id)).thenReturn(false);

        //When
        assertThatThrownBy(() -> underTest.deleteCustomerById(id))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Customer %s not found!".formatted(id));


        verify(customerDao, never()).deleteCustomer(id);

    }

    @Test
    void deleteCustomerById() {
        //Given
        int id = 10;

        when(customerDao.existPersonWithId(id)).thenReturn(true);

        underTest.deleteCustomerById(id);

        verify(customerDao).deleteCustomer(id);

    }

    @Test
    void canUpdateAllCustomersProperties() {
        // Given
        int id = 10;
        Customer customer = new Customer(
                 "Alex", "alex@gmail.com", "Mundburg 1", 19
        );
        when(customerDao.selectCustomerByID(id)).thenReturn(Optional.of(customer));

        String newEmail = "alexandro@amigoscode.com";

        CustomerUpdateRequest updateRequest = new CustomerUpdateRequest(
                "Alexandro", newEmail,"Mundburg 1",  23);

        when(customerDao.existsPersonWithEmail(newEmail)).thenReturn(false);

        // When
        underTest.updateCustomer(id, updateRequest);

        // Then
        ArgumentCaptor<Customer> customerArgumentCaptor =
                ArgumentCaptor.forClass(Customer.class);

        verify(customerDao).updateCustomer(customerArgumentCaptor.capture());
        Customer capturedCustomer = customerArgumentCaptor.getValue();

        assertThat(capturedCustomer.getName()).isEqualTo(updateRequest.name());
        assertThat(capturedCustomer.getEmail()).isEqualTo(updateRequest.email());
        assertThat(capturedCustomer.getAge()).isEqualTo(updateRequest.age());
    }

    @Test

    void canUpdateOnlyCustomerName() {
        // Given
        int id = 10;
        Customer customer = new Customer(
                 "Alex", "alex@gmail.com", "Mundburg 1", 19
        );
        when(customerDao.selectCustomerByID(id)).thenReturn(Optional.of(customer));

        CustomerUpdateRequest updateRequest = new CustomerUpdateRequest(
                "Alexandro", null, null, null);

        // When
        underTest.updateCustomer(id, updateRequest);

        // Then
        ArgumentCaptor<Customer> customerArgumentCaptor =
                ArgumentCaptor.forClass(Customer.class);

        verify(customerDao).updateCustomer(customerArgumentCaptor.capture());
        Customer capturedCustomer = customerArgumentCaptor.getValue();

        assertThat(capturedCustomer.getName()).isEqualTo(updateRequest.name());
        assertThat(capturedCustomer.getAge()).isEqualTo(customer.getAge());
        assertThat(capturedCustomer.getEmail()).isEqualTo(customer.getEmail());
    }

    @Test
    void canUpdateOnlyCustomerEmail() {
        // Given
        int id = 10;
        Customer customer = new Customer(
                "Alex", "alex@gmail.com", "Mundburg 1", 19
        );
        when(customerDao.selectCustomerByID(id)).thenReturn(Optional.of(customer));

        String newEmail = "alexandro@amigoscode.com";

        CustomerUpdateRequest updateRequest = new CustomerUpdateRequest(
                null, newEmail, null, null);

        when(customerDao.existsPersonWithEmail(newEmail)).thenReturn(false);

        // When
        underTest.updateCustomer(id, updateRequest);

        // Then
        ArgumentCaptor<Customer> customerArgumentCaptor =
                ArgumentCaptor.forClass(Customer.class);

        verify(customerDao).updateCustomer(customerArgumentCaptor.capture());
        Customer capturedCustomer = customerArgumentCaptor.getValue();

        assertThat(capturedCustomer.getName()).isEqualTo(customer.getName());
        assertThat(capturedCustomer.getAge()).isEqualTo(customer.getAge());
        assertThat(capturedCustomer.getEmail()).isEqualTo(newEmail);
    }

    @Test
    void canUpdateOnlyCustomerAge() {
        // Given
        int id = 10;
        Customer customer = new Customer(
                "Alex", "alex@gmail.com", "Mundburg 1", 19
        );
        when(customerDao.selectCustomerByID(id)).thenReturn(Optional.of(customer));

        CustomerUpdateRequest updateRequest = new CustomerUpdateRequest(
                null, null, null, 22);

        // When
        underTest.updateCustomer(id, updateRequest);

        // Then
        ArgumentCaptor<Customer> customerArgumentCaptor =
                ArgumentCaptor.forClass(Customer.class);

        verify(customerDao).updateCustomer(customerArgumentCaptor.capture());
        Customer capturedCustomer = customerArgumentCaptor.getValue();

        assertThat(capturedCustomer.getName()).isEqualTo(customer.getName());
        assertThat(capturedCustomer.getAge()).isEqualTo(updateRequest.age());
        assertThat(capturedCustomer.getEmail()).isEqualTo(customer.getEmail());
    }


    @Test
    void willThrowWhenCustomerUpdateHasNoChanges() {
        // Given
        int id = 10;
        Customer customer = new Customer(
              "Alex", "alex@gmail.com", "Mundburg 1", 19
        );
        when(customerDao.selectCustomerByID(id)).thenReturn(Optional.of(customer));

        CustomerUpdateRequest updateRequest = new CustomerUpdateRequest(
                customer.getName(),
                customer.getEmail(),
                customer.getAddress(),
                customer.getAge());

        // When
        assertThatThrownBy(() -> underTest.updateCustomer(id, updateRequest))
                .isInstanceOf(RequestValidationException.class)
                .hasMessage("Nothing to be changed!");

        // Then
        verify(customerDao, never()).updateCustomer(any());
    }
}