package com.shoaib.billing;

import java.util.List;

record Product(long id, String name, String hsn, double price, double gst, int stock) {}

record NewProduct(String name, String hsn, double price, double gst, int stock) {}

record Customer(String phone, String name, String gstin, String address) {}

record CartItem(String name, double price, int qty, double gst, String hsn) {}

record Invoice(
        long id,
        String customer,
        String customerPhone,
        String items,
        double total,
        double gstTotal,
        String date) {}

record NewInvoice(
        String customer,
        String customerPhone,
        List<CartItem> items,
        double total,
        double gstTotal) {}
