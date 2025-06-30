package com.UserService.Services;

import java.util.List;
import java.util.UUID;
import com.UserService.Dto.AddressDTO;

public interface AddressService {
    AddressDTO createAddress(AddressDTO dto);
    AddressDTO updateAddress(UUID id, AddressDTO dto);
    AddressDTO getAddressById(UUID id);
    List<AddressDTO> getAllAddresses();
    void deleteAddress(UUID id);
}