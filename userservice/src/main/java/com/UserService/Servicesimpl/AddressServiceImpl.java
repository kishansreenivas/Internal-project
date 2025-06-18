package com.UserService.Servicesimpl;

import com.UserService.Dto.AddressDTO;
import com.UserService.Entity.Address;
import com.UserService.Entity.User;
import com.UserService.Mapper.AddressMapper;
import com.UserService.Repositories.AddressRepository;
import com.UserService.Repositories.UserRepository;
import com.UserService.Services.AddressService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class AddressServiceImpl implements AddressService {

    private final AddressRepository addressRepository;
    private final UserRepository userRepository;
    private final AddressMapper addressMapper;

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
    public AddressDTO updateAddress(UUID id, AddressDTO dto) {
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

    @Override
    public AddressDTO getAddressById(UUID id) {
        log.info("ENTRY: getAddressById() - ID: {}", id);

        AddressDTO dto = addressRepository.findById(id)
                .map(addressMapper::toDto)
                .orElseThrow(() -> {
                    log.warn("Address not found: {}", id);
                    return new RuntimeException("Address not found");
                });

        log.info("EXIT: getAddressById() - Address: {}", dto);
        return dto;
    }

    @Override
    public List<AddressDTO> getAllAddresses() {
        log.info("ENTRY: getAllAddresses()");

        List<AddressDTO> addresses = addressRepository.findAll()
                .stream()
                .map(addressMapper::toDto)
                .collect(Collectors.toList());

        log.info("EXIT: getAllAddresses() - Total: {}", addresses.size());
        return addresses;
    }

    @Override
    public void deleteAddress(UUID id) {
        log.info("ENTRY: deleteAddress() - ID: {}", id);

        addressRepository.deleteById(id);

        log.info("EXIT: deleteAddress() - Deleted ID: {}", id);
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
