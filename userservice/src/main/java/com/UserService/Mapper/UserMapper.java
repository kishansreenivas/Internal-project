package com.UserService.Mapper;

//import java.util.ArrayList;
//import java.util.List;
//import java.util.stream.Collectors;
//import com.UserService.Dto.AddressDTO;
//import com.UserService.Dto.PaymentMethodDTO;
//import com.UserService.Dto.PreferencesDTO;
//import com.UserService.Entity.Address;
//import com.UserService.Entity.PaymentMethod;
//import com.UserService.Entity.Preferences;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.UserService.Entity.User;
import com.UserService.Dto.UserDTO;

@Component
public class UserMapper {
	@Autowired
	 private final ModelMapper modelMapper = new ModelMapper();

	    public UserDTO toDTO(User user) {
	        return modelMapper.map(user, UserDTO.class);
	    }

	    public User toEntity(UserDTO dto) {
	        return modelMapper.map(dto, User.class);
	    }

	    public void updateEntityFromDto(UserDTO dto, User user) {
	        modelMapper.map(dto, user);
	    }

}