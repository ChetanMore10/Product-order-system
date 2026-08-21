package com.product_order_system.serviceImpl;

import com.product_order_system.dto.request.AddressRequest;
import com.product_order_system.dto.response.AddressResponse;
import com.product_order_system.entity.Address;
import com.product_order_system.entity.User;
import com.product_order_system.exception.ResourceNotFoundException;
import com.product_order_system.repository.AddressRepository;
import com.product_order_system.repository.UserRepository;
import com.product_order_system.service.AddressService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class AddressServiceImpl implements AddressService {

    private final AddressRepository addressRepository;
    private final UserRepository userRepository;

    @Override
    public AddressResponse addAddress(
            Long userId,
            AddressRequest request) {

        User user = userRepository.findById(userId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "User not found with id: " + userId
                        ));

        Address address = new Address();

        address.setFullName(request.getFullName());
        address.setPhone(request.getPhone());
        address.setAddressLine(request.getAddressLine());
        address.setCity(request.getCity());
        address.setState(request.getState());
        address.setPincode(request.getPincode());
        address.setUser(user);

        Address savedAddress =
                addressRepository.save(address);

        return mapToResponse(savedAddress);
    }

    @Override
    @Transactional(readOnly = true)
    public List<AddressResponse> getUserAddresses(
            Long userId) {

        if (!userRepository.existsById(userId)) {
            throw new ResourceNotFoundException(
                    "User not found with id: " + userId
            );
        }

        return addressRepository.findByUserId(userId)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public AddressResponse getAddressById(
            Long userId,
            Long addressId) {

        Address address = addressRepository.findById(addressId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Address not found with id: "
                                        + addressId
                        ));

        validateAddressOwnership(address, userId);

        return mapToResponse(address);
    }

    @Override
    public AddressResponse updateAddress(
            Long userId,
            Long addressId,
            AddressRequest request) {

        Address address =
                addressRepository.findById(addressId)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Address not found with id: "
                                                + addressId
                                ));

        validateAddressOwnership(address, userId);

        address.setFullName(request.getFullName());
        address.setPhone(request.getPhone());
        address.setAddressLine(request.getAddressLine());
        address.setCity(request.getCity());
        address.setState(request.getState());
        address.setPincode(request.getPincode());

        Address updatedAddress =
                addressRepository.save(address);

        return mapToResponse(updatedAddress);
    }

    @Override
    public void deleteAddress(
            Long userId,
            Long addressId) {

        Address address =
                addressRepository.findById(addressId)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Address not found with id: "
                                                + addressId
                                ));

        validateAddressOwnership(address, userId);

        addressRepository.delete(address);
    }

    private void validateAddressOwnership(
            Address address,
            Long userId) {

        if (!address.getUser().getId().equals(userId)) {

            throw new ResourceNotFoundException(
                    "Address not found for this user"
            );
        }
    }

    private AddressResponse mapToResponse(
            Address address) {

        return new AddressResponse(
                address.getId(),
                address.getFullName(),
                address.getPhone(),
                address.getAddressLine(),
                address.getCity(),
                address.getState(),
                address.getPincode()
        );
    }
}
