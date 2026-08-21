package com.product_order_system.service;

import com.product_order_system.dto.request.AddressRequest;
import com.product_order_system.dto.response.AddressResponse;

import java.util.List;

public interface AddressService {

    AddressResponse addAddress(Long userId, AddressRequest request);

    List<AddressResponse> getUserAddresses(Long userId);

    AddressResponse getAddressById(Long userId, Long addressId);

    AddressResponse updateAddress(Long userId, Long addressId, AddressRequest request);

    void deleteAddress(Long userId, Long addressId);
}