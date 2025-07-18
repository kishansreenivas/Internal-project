package com.UserService.Servicesimpl;


import java.util.Optional;

import org.springframework.stereotype.Service;

import com.UserService.Dto.AddressDTO;
import com.UserService.Entity.Address;
import com.UserService.Entity.User;
import com.UserService.Mapper.AddressMapper;
import com.UserService.Repositories.AddressRepository;
import com.UserService.Repositories.UserRepository;
import com.UserService.Services.AddressService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class AddressServiceImpl implements AddressService {

    private final AddressRepository addressRepository;
    private final UserRepository userRepository;
    private final AddressMapper addressMapper;



    @Override
	public Optional<Address> findById(Long id) {
        return addressRepository.findById(id);
    }

    @Override
    public AddressDTO createAddress(AddressDTO dto) {
        log.info("ENTRY: createAddress() - DTO: {}", dto);

        validateAddressDTO(dto);

        User user = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> {
                    log.warn("User not found: {}", dto.getUserId());
                    return new RuntimeException("User not found");
                });

        Address address = addressMapper.toEntity(dto, user);
        AddressDTO savedDto = addressMapper.toDto(addressRepository.save(address));

        log.info("EXIT: createAddress() - Saved Address: {}", savedDto);
        return savedDto;
    }

    @Override
    public AddressDTO updateAddress(Long id, AddressDTO dto) {
        log.info("ENTRY: updateAddress() - ID: {}, DTO: {}", id, dto);

        validateAddressDTO(dto);

        Address existing = addressRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Address not found: {}", id);
                    return new RuntimeException("Address not found");
                });

        User user = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> {
                    log.warn("User not found: {}", dto.getUserId());
                    return new RuntimeException("User not found");
                });

        existing.setStreet(dto.getStreet());
        existing.setCity(dto.getCity());
        existing.setState(dto.getState());
        existing.setPostalCode(dto.getPostalCode());
        existing.setUser(user);

        AddressDTO updatedDto = addressMapper.toDto(addressRepository.save(existing));

        log.info("EXIT: updateAddress() - Updated Address: {}", updatedDto);
        return updatedDto;
    }


    // Validation method
    private void validateAddressDTO(AddressDTO dto) {
        if (dto.getStreet() == null || dto.getStreet().isEmpty()) {
            log.warn("Validation failed: Street is required");
            throw new IllegalArgumentException("Street is required");
        }
        if (dto.getCity() == null || dto.getCity().isEmpty()) {
            log.warn("Validation failed: City is required");
            throw new IllegalArgumentException("City is required");
        }
        if (dto.getState() == null || dto.getState().isEmpty()) {
            log.warn("Validation failed: State is required");
            throw new IllegalArgumentException("State is required");
        }
        if (dto.getPostalCode() == null || dto.getPostalCode().isEmpty()) {
            log.warn("Validation failed: Postal code is required");
            throw new IllegalArgumentException("Postal code is required");
        }
        if (dto.getUserId() == null) {
            log.warn("Validation failed: User ID is required");
            throw new IllegalArgumentException("User ID is required");
        }

        log.debug("Validation passed for AddressDTO: {}", dto);
    }


}
