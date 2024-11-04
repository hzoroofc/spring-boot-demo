package de.hzoroofchi.customer;

import com.github.javafaker.Faker;
import jakarta.persistence.*;

import java.util.Objects;

@Entity
@Table(
        name = "customer",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "customer_email_unique",
                        columnNames = "email"
                )
        }
)
public class Customer {
    @Column(
            nullable = false
    )
    String name;
    @Column(
            nullable = false,
            unique = true
    )
    int age;

    @Column
    String address;

    @Id
    @SequenceGenerator(
            name = "customer_id_seq",
            sequenceName = "customer_id_seq",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "customer_id_seq"
    )
    private int id;

    @Column
    String email;

    public Customer(String name, String email, String address, int age, int id) {
        this.name = name;
        this.age = age;
        this.address = address;
        this.id = id;
        this.email = email;
    }

    public Customer() {

    }

    public Customer(String name, String email, String address, int age) {
        this.name = name;
        this.age = age;
        this.address = address;
        this.email = email;

    }public Customer(String name, String email, int age) {
        this.name = name;
        this.age = age;
        this.address = new Faker().address().fullAddress();
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @Override
    public String toString() {
        return "Customers{" +
                "name='" + name + '\'' +
                ", age=" + age +
                ", address='" + address + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Customer customers = (Customer) o;
        return getAge() == customers.getAge() && Objects.equals(getName(), customers.getName()) && Objects.equals(getAddress(), customers.getAddress());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getName(), getAge(), getAddress());
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
