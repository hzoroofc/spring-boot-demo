package de.hzoroofchi.customer;

import de.hzoroofchi.AbstractTestcontainers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;


class CustomerJDBCDataAccessServiceTest extends AbstractTestcontainers {


    private  CustomerJDBCDataAccessService underTest;
    private final CustomerRowMapper customerRowMapper = new CustomerRowMapper();

    @BeforeEach
    void setUp() {
        underTest = new CustomerJDBCDataAccessService(
                customerRowMapper,
                getJdbcTemplate()
        );
    }

    @Test
    void selectAllCustomers() {
        //Giver
        Customer customer = new Customer(
                FAKER.name().fullName(),
                FAKER.internet().safeEmailAddress() + "-" + UUID.randomUUID(),
                FAKER.address().fullAddress(),
                20
        );

        underTest.inserCustomer(customer);

        List<Customer> actual = underTest.selectAllCustomers();

        assertThat(actual).isNotEmpty();
    }

    @Test
    void selectCustomerByID() {

        String email = FAKER.internet().safeEmailAddress() + "-" + UUID.randomUUID();

        Customer customer = new Customer(
                FAKER.name().fullName(),
                email,
                FAKER.address().fullAddress(),
                20
        );

        underTest.inserCustomer(customer);

        System.out.println(underTest.selectAllCustomers());


        Integer id = underTest.selectAllCustomers()
                .stream()
                .filter(customer1 -> customer1.getEmail().equals(email))
                .map(Customer::getId)
                .findFirst()
                .orElseThrow();

        Optional<Customer> actual = underTest.selectCustomerByID(id);

        assertThat(actual).isPresent().hasValueSatisfying(c ->{
           assertThat(c.getId()).isEqualTo(id);
           assertThat(c.getName()).isEqualTo(customer.getName());
           assertThat(c.getAge()).isEqualTo(customer.getAge());
           assertThat(c.getEmail()).isEqualTo(customer.getEmail());
           assertThat(c.getAddress()).isEqualTo(customer.getAddress());
        });
    }


    @Test
    void inserCustomer() {

        String email = FAKER.internet().safeEmailAddress() + "-" + UUID.randomUUID();

        Customer customer = new Customer(
                FAKER.name().fullName(),
                email,
                FAKER.address().fullAddress(),
                20
        );

        underTest.inserCustomer(customer);

        Integer id = underTest.selectAllCustomers()
                .stream()
                .filter(customer1 -> customer1.getEmail().equals(email))
                .map(Customer::getId)
                .findFirst()
                .orElseThrow();

        Optional<Customer> actual = underTest.selectCustomerByID(id);

        assertThat(actual).isPresent().hasValueSatisfying(c -> assertThat(c.getEmail()).isEqualTo(email));
    }

    @Test
    void existsPersonWithEmail() {
        //Giver
        String email = FAKER.internet().safeEmailAddress();
        Customer customer = new Customer(
                FAKER.name().fullName(),
                email,
                FAKER.address().fullAddress(),
                28
        );

        underTest.inserCustomer(customer);
        //When
        boolean actual = underTest.existsPersonWithEmail(email);
        //Then
        assertThat(actual).isTrue();

    }


    @Test
    void updateCustomerEmail() {
        // Given
        String email = FAKER.internet().safeEmailAddress() + "-" + UUID.randomUUID();
        Customer customer = new Customer(
                FAKER.name().fullName(),
                email,
                20
        );

        underTest.inserCustomer(customer);
        int id = underTest.selectAllCustomers()
                .stream()
                .filter(c -> c.getEmail().equals(email))
                .map(Customer::getId)
                .findFirst()
                .orElseThrow();

        var newEmail = FAKER.internet().safeEmailAddress() + "-" + UUID.randomUUID();

        // When email is changed
        Customer update = new Customer();
        update.setId(id);
        update.setEmail(newEmail);
        underTest.updateCustomer(update);
        // Then
        Optional<Customer> actual = underTest.selectCustomerByID(id);
        assertThat(actual).isPresent().hasValueSatisfying(c -> {
            assertThat(c.getEmail()).isEqualTo(update.getEmail()); // change
        });
    }

    @Test
    void willNotUpdateWhenNothingToUpdate() {
        // Given
        String email = FAKER.internet().safeEmailAddress() + "-" + UUID.randomUUID();
        Customer customer = new Customer(
                FAKER.name().fullName(),
                email,
                20
        );
        underTest.inserCustomer(customer);
        int id = underTest.selectAllCustomers()
                .stream()
                .filter(c -> c.getEmail().equals(email))
                .map(Customer::getId)
                .findFirst()
                .orElseThrow();
        // When update without no changes
        Customer update = new Customer();
        update.setId(id);
        underTest.updateCustomer(update);
        // Then
        Optional<Customer> actual = underTest.selectCustomerByID(id);
        assertThat(actual).isPresent().hasValueSatisfying(c -> {
            assertThat(c.getId()).isEqualTo(id);
            assertThat(c.getAge()).isEqualTo(customer.getAge());
            assertThat(c.getName()).isEqualTo(customer.getName());
            assertThat(c.getEmail()).isEqualTo(customer.getEmail());
        });
    }

    @Test
    void willUpdateAllPropertiesCustomer() {
        // Given
        String email = FAKER.internet().safeEmailAddress() + "-" + UUID.randomUUID();
        Customer customer = new Customer(
                FAKER.name().fullName(),
                email,
                FAKER.address().fullAddress(),
                20
        );
        underTest.inserCustomer(customer);


        int id = underTest.selectAllCustomers()
                .stream()
                .filter(c -> c.getEmail().equals(email))
                .map(Customer::getId)
                .findFirst()
                .orElseThrow();

        System.out.println("id: " + id);
        // When update with new name, age and email
        Customer update = new Customer();
        update.setId(id);
        update.setName("foo");
        update.setEmail(UUID.randomUUID().toString());
        update.setAddress(FAKER.address().fullAddress());
        update.setAge(22);
        underTest.updateCustomer(update);

        System.out.println(update);
        System.out.println(underTest.selectAllCustomers());

        // Then
        Optional<Customer> actual = underTest.selectCustomerByID(id);

        System.out.println(actual);


        assertThat(actual).isPresent().hasValue(update);
    }

    @Test
    void updateCustomerAge() {
        // Given
        String email = FAKER.internet().safeEmailAddress() + "-" + UUID.randomUUID();
        Customer customer = new Customer(
                FAKER.name().fullName(),
                email,
                20
        );
        underTest.inserCustomer(customer);
        int id = underTest.selectAllCustomers()
                .stream()
                .filter(c -> c.getEmail().equals(email))
                .map(Customer::getId)
                .findFirst()
                .orElseThrow();
        var newAge = 100;
        // When age is changed
        Customer update = new Customer();
        update.setId(id);
        update.setAge(newAge);
        underTest.updateCustomer(update);
        // Then
        Optional<Customer> actual = underTest.selectCustomerByID(id);
        assertThat(actual).isPresent().hasValueSatisfying(c -> {
            assertThat(c.getId()).isEqualTo(id);
            assertThat(c.getAge()).isEqualTo(newAge); // change
            assertThat(c.getName()).isEqualTo(customer.getName());
            assertThat(c.getEmail()).isEqualTo(customer.getEmail());
        });
    }

    @Test
    void updateCustomerName() {
        // Given
        String email = FAKER.internet().safeEmailAddress() + "-" + UUID.randomUUID();
        Customer customer = new Customer(
                FAKER.name().fullName(),
                email,
                20
        );
        underTest.inserCustomer(customer);
        int id = underTest.selectAllCustomers()
                .stream()
                .filter(c -> c.getEmail().equals(email))
                .map(Customer::getId)
                .findFirst()
                .orElseThrow();
        var newName = "foo";
        // When age is name
        Customer update = new Customer();
        update.setId(id);
        update.setName(newName);
        underTest.updateCustomer(update);
        // Then
        Optional<Customer> actual = underTest.selectCustomerByID(id);
        assertThat(actual).isPresent().hasValueSatisfying(c -> {
            assertThat(c.getId()).isEqualTo(id);
            assertThat(c.getName()).isEqualTo(newName); // change
            assertThat(c.getEmail()).isEqualTo(customer.getEmail());
            assertThat(c.getAge()).isEqualTo(customer.getAge());
        });
    }

    @Test
    void deleteCustomerById() {
        // Given
        String email = FAKER.internet().safeEmailAddress() + "-" + UUID.randomUUID();
        Customer customer = new Customer(
                FAKER.name().fullName(),
                email,
                20
        );
        underTest.inserCustomer(customer);
        int id = underTest.selectAllCustomers()
                .stream()
                .filter(c -> c.getEmail().equals(email))
                .map(Customer::getId)
                .findFirst()
                .orElseThrow();
        // When
        underTest.deleteCustomer(id);
        // Then
        Optional<Customer> actual = underTest.selectCustomerByID(id);
        assertThat(actual).isNotPresent();
    }

    @Test
    void existsPersonWithIdWillReturnFalseWhenIdNotPresent() {
        // Given
        int id = -1;
        // When
        var actual = underTest.existPersonWithId(id);
        // Then
        assertThat(actual).isFalse();
    }
}