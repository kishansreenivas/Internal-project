package com.UserService.Mapper;


import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.UserService.Dto.AddressDTO;
import com.UserService.Dto.UserDTO;
import com.UserService.Entity.Address;
import com.UserService.Entity.User;

@Component
public class UserMapper {

    @Autowired private ModelMapper modelMapper;

    public UserDTO toDTO(User user) {
        UserDTO dto = modelMapper.map(user, UserDTO.class);

        // Manually map addresses if needed
        if (user.getAddresses() != null) {
            List<AddressDTO> addressDTOs = user.getAddresses().stream()
                .map(address -> modelMapper.map(address, AddressDTO.class))
                .collect(Collectors.toList());
            dto.setAddresses(addressDTOs);
        }

        return dto;
    }

    public User toEntity(UserDTO dto) {
        User user = modelMapper.map(dto, User.class);

        // Set username from email
        user.setUsername(dto.getEmail());

        // Handle nested addresses
        if (dto.getAddresses() != null) {
            List<Address> addresses = dto.getAddresses().stream()
                .map(addressDto -> {
                    Address address = modelMapper.map(addressDto, Address.class);
                    address.setUser(user);
                    return address;
                }).collect(Collectors.toList());
            user.setAddresses(addresses);
        }

        return user;
    }
}
