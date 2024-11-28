package de.hzoroofchi;

import com.github.javafaker.Faker;
import de.hzoroofchi.customer.Customer;
import de.hzoroofchi.customer.CustomerRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@SpringBootApplication
public class Main {
    public static void main(String[] args) {
        ConfigurableApplicationContext configurableApplicationContext = SpringApplication.run(Main.class, args);
    }

    @Bean
    CommandLineRunner runner(CustomerRepository customerRepository) {
        return args -> {

            Faker faker = new Faker();
            var name = faker.name();
            Random random = new Random();

            Customer customer = new Customer(
                    faker.name().fullName(),
                    faker.internet().emailAddress(),
                    faker.address().fullAddress(),
                    random.nextInt(16, 99)
            );

            /*List<Customer> customers = new ArrayList<>();
            customers.add(new Customer("Jamila", "jamila@web.de", "Rechterallee 28", 28));
            customers.add(new Customer("Ali", "alite@gmx.de", "Bürger Straße 33", 31));
            customers.add(new Customer("Leonardo", "leo@web.de", "Am Aker 1", 20));*/
            customerRepository.save(customer);
        };
    }


@Bean
    public Foo getFoo() {
        return new Foo("Hallo");
    }

    record Foo(String name){}
}