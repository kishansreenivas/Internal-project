package com.UserService.Mapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.UserService.Dto.AddressDTO;
import com.UserService.Entity.Address;
import com.UserService.Entity.User;

@Component
public class AddressMapper {
    public AddressDTO toDto(Address address) {
        AddressDTO dto = new AddressDTO();
        dto.setId(address.getId());
        dto.setStreet(address.getStreet());
        dto.setCity(address.getCity());
        dto.setState(address.getState());
        dto.setPostalCode(address.getPostalCode());
        if (address.getUser() != null) {
            dto.setUserId(address.getUser().getId());
        }
        return dto;
    }

    public Address toEntity(AddressDTO dto, User user) {
        Address address = new Address();
        address.setId(dto.getId());
        address.setStreet(dto.getStreet());
        address.setCity(dto.getCity());
        address.setState(dto.getState());
        address.setPostalCode(dto.getPostalCode());
        address.setUser(user);
        return address;
    }
}
