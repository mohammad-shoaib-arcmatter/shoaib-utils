package com.shoaib.billing;

import com.fasterxml.jackson.core.JsonProcessingException;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
class BillingController {
    private final BillingService billingService;

    BillingController(BillingService billingService) {
        this.billingService = billingService;
    }

    @GetMapping("/products")
    List<Product> getProducts() {
        return billingService.getProducts();
    }

    @PostMapping("/products")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void addProduct(@RequestBody NewProduct product) {
        billingService.addProduct(product);
    }

    @DeleteMapping("/products/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void deleteProduct(@PathVariable long id) {
        billingService.deleteProduct(id);
    }

    @GetMapping("/customers")
    List<Customer> getCustomers() {
        return billingService.getCustomers();
    }

    @GetMapping("/customers/search")
    List<Customer> searchCustomers(@RequestParam String query) {
        return billingService.searchCustomers(query);
    }

    @PostMapping("/customers")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void addCustomer(@RequestBody Customer customer) {
        billingService.addCustomer(customer);
    }

    @PostMapping("/invoices")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void createInvoice(@RequestBody NewInvoice invoice) throws JsonProcessingException {
        billingService.createInvoice(invoice);
    }

    @GetMapping("/invoices")
    List<Invoice> getInvoices() {
        return billingService.getInvoices();
    }
}
