package com.UserService.Mapper;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.UserService.Dto.AddressDTO;
import com.UserService.Entity.Address;
import com.UserService.Entity.User;

@Component
public class AddressMapper {

	 private final ModelMapper modelMapper;

	    @Autowired
	    public AddressMapper(ModelMapper modelMapper) {
	        this.modelMapper = modelMapper;
	    }

	    public AddressDTO toDto(Address address) {
	        AddressDTO dto = modelMapper.map(address, AddressDTO.class);
	        if (address.getUser() != null) {
	            dto.setUserId(address.getUser().getId()); // Manually set userId
	        }
	        return dto;
	    }

	    public Address toEntity(AddressDTO dto, User user) {
	        Address address = modelMapper.map(dto, Address.class);
	        address.setUser(user); // Manually set user reference
	        return address;
	    }


}
