package com.UserService.Services;

import java.util.Optional;

import com.UserService.Dto.AddressDTO;
import com.UserService.Entity.Address;

public interface AddressService {


    public Optional<Address> findById(Long id) ;

    AddressDTO createAddress(AddressDTO dto);
    AddressDTO updateAddress(Long id, AddressDTO dto);

}
