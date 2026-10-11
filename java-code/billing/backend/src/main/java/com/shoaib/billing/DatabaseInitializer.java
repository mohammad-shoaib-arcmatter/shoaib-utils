package com.shoaib.billing;

import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

@Configuration
class DatabaseInitializer {
    @Bean
    ApplicationRunner initializeDatabase(JdbcTemplate jdbc) {
        return args -> {
            jdbc.execute("""
                    CREATE TABLE IF NOT EXISTS products (
                      id INTEGER PRIMARY KEY AUTOINCREMENT,
                      name TEXT, hsn TEXT, price REAL, gst REAL, stock INTEGER
                    )
                    """);
            jdbc.execute("""
                    CREATE TABLE IF NOT EXISTS customers (
                      phone TEXT PRIMARY KEY,
                      name TEXT NOT NULL,
                      gstin TEXT,
                      address TEXT
                    )
                    """);
            jdbc.execute("""
                    CREATE TABLE IF NOT EXISTS invoices (
                      id INTEGER PRIMARY KEY AUTOINCREMENT,
                      customer TEXT, customer_phone TEXT, items TEXT,
                      total REAL, gst_total REAL, date TEXT
                    )
                    """);
        };
    }
}
