package com.UserService.Mapper;


import java.util.List;
import java.util.stream.Collectors;
import com.UserService.Dto.AddressDTO;


import com.UserService.Entity.Address;


import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.UserService.Entity.User;
import com.UserService.Exception.ResourceNotFoundException;
import com.UserService.Services.AddressService;
import com.UserService.Servicesimpl.AddressServiceImpl;
import com.UserService.Dto.UserDTO;

@Component
public class UserMapper {

	 @Autowired private  ModelMapper modelMapper;
	 @Autowired private AddressServiceImpl addressService;

	    @Autowired
	    public UserMapper(ModelMapper modelMapper) {
	        this.modelMapper = modelMapper;
	    }

	    public UserDTO toDTO(User user) {
	        UserDTO dto = modelMapper.map(user, UserDTO.class);
	        // Manually map nested addresses
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

	        // Set user reference in addresses
	        if (dto.getAddresses() != null) {
	            List<Address> addresses = dto.getAddresses().stream()
	                .map(addressDto -> {
	                    Address address = modelMapper.map(addressDto, Address.class);
	                    address.setUser(user); // Set user manually
	                    return address;
	                }).collect(Collectors.toList());
	            user.setAddresses(addresses);
	        }

	        return user;
	    }

	    public void updateEntityFromDto(UserDTO dto, User user) {
	        if (dto == null || user == null) return;

	        // Ensure the ID is not overwritten
	        if (dto.getId() != null) {
	            user.setId(dto.getId()); // Do not set this; `id` should never be changed during updates
	        }

	        // Update other fields
	        if (dto.getFirstName() != null) {
	            user.setFirstName(dto.getFirstName());  // Update first name
	        }

	        if (dto.getLastName() != null) {
	            user.setLastName(dto.getLastName());  // Update last name
	        }

	        if (dto.getEmail() != null) {
	            user.setEmail(dto.getEmail());  // Update email
	        }

	        if (dto.getPassword() != null) {
	            user.setPassword(dto.getPassword());  // Update password (consider encrypting it)
	        }

	        if (dto.getPhone() != null) {
	            user.setPhone(dto.getPhone());  // Update phone number
	        }

	        // Assuming you have a method to update the addresses (not just setting it directly)
	        if (dto.getAddresses() != null) {
	            // Assuming you have an address service or method to update user addresses
	            for (AddressDTO addressDto : dto.getAddresses()) {
	                // You can either update existing addresses or add new ones, depending on your business logic
	                // For example:
	                if (addressDto.getId() != null) {
	                    // Update existing address
	                    Address address = addressService.findById(addressDto.getId())
	                            .orElseThrow(() -> new ResourceNotFoundException("Address not found"));

	                    address.setStreet(addressDto.getStreet());
	                    address.setCity(addressDto.getCity());
	                    address.setState(addressDto.getState());
	                    address.setPostalCode(addressDto.getPostalCode());
	                    // Update address in the user
	                    user.getAddresses().add(address);
	                } else {
	                    // Create new address and add it to the user
	                    Address newAddress = new Address();
	                    newAddress.setStreet(addressDto.getStreet());
	                    newAddress.setCity(addressDto.getCity());
	                    newAddress.setState(addressDto.getState());
	                    newAddress.setPostalCode(addressDto.getPostalCode());
	                    newAddress.setUser(user);  // Set the user to the new address
	                    user.getAddresses().add(newAddress);
	                }
	            }
	        }

	        
	    }


    
}
