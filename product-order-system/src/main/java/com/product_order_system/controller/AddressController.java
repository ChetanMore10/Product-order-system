package com.product_order_system.controller;

import com.product_order_system.dto.request.AddressRequest;
import com.product_order_system.dto.response.AddressResponse;
import com.product_order_system.service.AddressService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/addresses")
@RequiredArgsConstructor
public class AddressController {

    private final AddressService addressService;

    @PostMapping
    public ResponseEntity<AddressResponse> addAddress(@RequestParam Long userId, @Valid @RequestBody AddressRequest request) {

        return ResponseEntity.status(HttpStatus.CREATED).body(addressService.addAddress(userId, request));
    }

    @GetMapping
    public ResponseEntity<List<AddressResponse>> getUserAddresses(@RequestParam Long userId) {

        return ResponseEntity.ok(addressService.getUserAddresses(userId));
    }

    @GetMapping("/{addressId}")
    public ResponseEntity<AddressResponse> getAddressById(@RequestParam Long userId, @PathVariable Long addressId) {

        return ResponseEntity.ok(addressService.getAddressById(userId, addressId));
    }

    @PutMapping("/{addressId}")
    public ResponseEntity<AddressResponse> updateAddress(@RequestParam Long userId, @PathVariable Long addressId, @Valid @RequestBody AddressRequest request) {

        return ResponseEntity.ok(addressService.updateAddress(userId, addressId, request));
    }

    @DeleteMapping("/{addressId}")
    public ResponseEntity<Void> deleteAddress(@RequestParam Long userId, @PathVariable Long addressId) {

        addressService.deleteAddress(userId, addressId);

        return ResponseEntity.noContent().build();
    }
}