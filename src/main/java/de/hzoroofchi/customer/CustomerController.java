package de.hzoroofchi.customer;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/customers/")
public class CustomerController {

    private final CustomerService customerService;

    @Autowired
    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @GetMapping("")
    public List<Customer> CustomersResponse() {
        return customerService.selectAllCustomers();
    }

    @GetMapping("{customerId}")
    public Optional<Customer> CustomerResponse(
            @PathVariable("customerId") Integer customerId
    ) {
        return customerService.selectCustomerById(customerId);
    }


    @DeleteMapping("{customerId}")
    public void deleteCustomerById(
            @PathVariable("customerId") Integer customerId) {
        customerService.deleteCustomerById(customerId);
    }

    @PostMapping
    public void registerCustomer(@RequestBody CustomerRegistretionRequest customerRegistretionRequest) {
        customerService.addCustomer(customerRegistretionRequest);
    }

    @PutMapping("{customerId}")
    public void updateCustomer(@RequestBody CustomerUpdateRequest updateRequest,
                               @PathVariable Integer customerId) {
        customerService.updateCustomer(customerId, updateRequest);
    }

}
