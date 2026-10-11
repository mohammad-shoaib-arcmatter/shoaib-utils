package com.shoaib.billing;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.Instant;
import java.util.List;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

@Service
class BillingService {
    private final JdbcTemplate jdbc;
    private final ObjectMapper objectMapper;

    BillingService(JdbcTemplate jdbc, ObjectMapper objectMapper) {
        this.jdbc = jdbc;
        this.objectMapper = objectMapper;
    }

    List<Product> getProducts() {
        return jdbc.query(
                "SELECT id, name, hsn, price, gst, stock FROM products",
                (row, index) -> new Product(
                        row.getLong("id"),
                        row.getString("name"),
                        row.getString("hsn"),
                        row.getDouble("price"),
                        row.getDouble("gst"),
                        row.getInt("stock")));
    }

    void addProduct(NewProduct product) {
        jdbc.update(
                "INSERT INTO products (name, hsn, price, gst, stock) VALUES (?, ?, ?, ?, ?)",
                product.name(),
                product.hsn(),
                product.price(),
                product.gst(),
                product.stock());
    }

    void deleteProduct(long id) {
        jdbc.update("DELETE FROM products WHERE id = ?", id);
    }

    List<Customer> getCustomers() {
        return jdbc.query(
                "SELECT phone, name, gstin, address FROM customers ORDER BY name",
                (row, index) -> new Customer(
                        row.getString("phone"),
                        row.getString("name"),
                        row.getString("gstin"),
                        row.getString("address")));
    }

    List<Customer> searchCustomers(String query) {
        String pattern = "%" + query + "%";
        return jdbc.query(
                "SELECT phone, name, gstin, address FROM customers WHERE name LIKE ? OR phone LIKE ?",
                (row, index) -> new Customer(
                        row.getString("phone"),
                        row.getString("name"),
                        row.getString("gstin"),
                        row.getString("address")),
                pattern,
                pattern);
    }

    void addCustomer(Customer customer) {
        if (customer.phone() == null || customer.phone().isBlank()
                || customer.name() == null || customer.name().isBlank()) {
            throw new IllegalArgumentException("Phone and Name required");
        }
        jdbc.update(
                "INSERT OR REPLACE INTO customers (phone, name, gstin, address) VALUES (?, ?, ?, ?)",
                customer.phone(),
                customer.name(),
                customer.gstin() == null ? "" : customer.gstin(),
                customer.address() == null ? "" : customer.address());
    }

    void createInvoice(NewInvoice invoice) throws JsonProcessingException {
        if (invoice.customer() == null || invoice.customer().isBlank()
                || invoice.customerPhone() == null || invoice.customerPhone().isBlank()
                || invoice.items() == null || invoice.items().isEmpty()) {
            throw new IllegalArgumentException("Customer and invoice items are required");
        }
        jdbc.update(
                "INSERT INTO invoices (customer, customer_phone, items, total, gst_total, date) VALUES (?, ?, ?, ?, ?, ?)",
                invoice.customer(),
                invoice.customerPhone(),
                objectMapper.writeValueAsString(invoice.items()),
                invoice.total(),
                invoice.gstTotal(),
                Instant.now().toString());
    }

    List<Invoice> getInvoices() {
        return jdbc.query(
                "SELECT id, customer, customer_phone, items, total, gst_total, date FROM invoices ORDER BY id DESC",
                (row, index) -> new Invoice(
                        row.getLong("id"),
                        row.getString("customer"),
                        row.getString("customer_phone"),
                        row.getString("items"),
                        row.getDouble("total"),
                        row.getDouble("gst_total"),
                        row.getString("date")));
    }
}
