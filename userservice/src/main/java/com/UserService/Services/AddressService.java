package com.UserService.Services;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;

import com.UserService.Dto.AddressDTO;
import com.UserService.Entity.Address;
import com.UserService.Repositories.AddressRepository;

public interface AddressService {
	
	
    public Optional<Address> findById(Long id) ;

    AddressDTO createAddress(AddressDTO dto);
    AddressDTO updateAddress(Long id, AddressDTO dto);

}
