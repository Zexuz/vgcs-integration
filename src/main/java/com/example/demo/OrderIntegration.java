package com.example.demo;

public record OrderIntegration(
        String id,
        String firstName,
        String lastName,
        String email,
        String supplierPid,
        String creditCardNumber,
        String creditCardType,
        String orderId,
        String productPid,
        String shippingAddress,
        String country,
        String dateCreated,
        String quantity,
        String fullName,
        String orderStatus
) {

}
