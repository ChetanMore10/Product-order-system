package com.product_order_system.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class AddressResponse {

    private Long id;

    private String fullName;

    private String phone;

    private String addressLine;

    private String city;

    private String state;

    private String pincode;
}