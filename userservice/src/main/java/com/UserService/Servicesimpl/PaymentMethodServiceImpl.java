package com.UserService.Servicesimpl;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.UserService.Dto.PaymentMethodDTO;
import com.UserService.Entity.PaymentMethod;
import com.UserService.Entity.User;
import com.UserService.Mapper.PaymentMethodMapper;
import com.UserService.Repositories.PaymentMethodRepository;
import com.UserService.Repositories.UserRepository;
import com.UserService.Services.PaymentMethodService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class PaymentMethodServiceImpl implements PaymentMethodService {

    private final PaymentMethodRepository paymentMethodRepository;
    private final UserRepository userRepository;
    private final PaymentMethodMapper paymentMethodMapper;

    @Override
    public PaymentMethodDTO createPaymentMethod(PaymentMethodDTO dto) {
        log.info("ENTRY: createPaymentMethod() - DTO: {}", dto);

        validatePaymentMethodDTO(dto);

        User user = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> {
                    log.warn("User not found: {}", dto.getUserId());
                    return new RuntimeException("User not found");
                });

        PaymentMethod paymentMethod = paymentMethodMapper.toEntity(dto, user);
        PaymentMethodDTO savedDto = paymentMethodMapper.toDto(paymentMethodRepository.save(paymentMethod));

        log.info("EXIT: createPaymentMethod() - Saved PaymentMethod: {}", savedDto);
        return savedDto;
    }

    @Override
    public PaymentMethodDTO updatePaymentMethod(UUID id, PaymentMethodDTO dto) {
        log.info("ENTRY: updatePaymentMethod() - ID: {}, DTO: {}", id, dto);

        validatePaymentMethodDTO(dto);

        PaymentMethod existing = paymentMethodRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("PaymentMethod not found: {}", id);
                    return new RuntimeException("PaymentMethod not found");
                });

        User user = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> {
                    log.warn("User not found: {}", dto.getUserId());
                    return new RuntimeException("User not found");
                });

        existing.setCardNumber(dto.getCardNumber());
        existing.setCardHolder(dto.getCardHolder());
        existing.setExpiry(dto.getExpiry());
        existing.setType(dto.getType());
        existing.setUser(user);

        PaymentMethodDTO updatedDto = paymentMethodMapper.toDto(paymentMethodRepository.save(existing));

        log.info("EXIT: updatePaymentMethod() - Updated PaymentMethod: {}", updatedDto);
        return updatedDto;
    }

    @Override
    public PaymentMethodDTO getPaymentMethodById(UUID id) {
        log.info("ENTRY: getPaymentMethodById() - ID: {}", id);

        PaymentMethodDTO dto = paymentMethodRepository.findById(id)
                .map(paymentMethodMapper::toDto)
                .orElseThrow(() -> {
                    log.warn("PaymentMethod not found: {}", id);
                    return new RuntimeException("PaymentMethod not found");
                });

        log.info("EXIT: getPaymentMethodById() - PaymentMethod: {}", dto);
        return dto;
    }

    @Override
    public List<PaymentMethodDTO> getAllPaymentMethods() {
        log.info("ENTRY: getAllPaymentMethods()");

        List<PaymentMethodDTO> list = paymentMethodRepository.findAll()
                .stream()
                .map(paymentMethodMapper::toDto)
                .collect(Collectors.toList());

        log.info("EXIT: getAllPaymentMethods() - Total: {}", list.size());
        return list;
    }

    @Override
    public void deletePaymentMethod(UUID id) {
        log.info("ENTRY: deletePaymentMethod() - ID: {}", id);
        paymentMethodRepository.deleteById(id);
        log.info("EXIT: deletePaymentMethod() - Deleted ID: {}", id);
    }

    private void validatePaymentMethodDTO(PaymentMethodDTO dto) {
        if (dto.getCardNumber() == null || dto.getCardNumber().isEmpty()) {
            log.warn("Validation failed: Card number is required");
            throw new IllegalArgumentException("Card number is required");
        }
        if (dto.getCardHolder() == null || dto.getCardHolder().isEmpty()) {
            log.warn("Validation failed: Card holder is required");
            throw new IllegalArgumentException("Card holder is required");
        }
        if (dto.getExpiry() == null || dto.getExpiry().isEmpty()) {
            log.warn("Validation failed: Expiry is required");
            throw new IllegalArgumentException("Expiry date is required");
        }
        if (dto.getType() == null || dto.getType().isEmpty()) {
            log.warn("Validation failed: Card type is required");
            throw new IllegalArgumentException("Card type is required");
        }
        if (dto.getUserId() == null) {
            log.warn("Validation failed: User ID is required");
            throw new IllegalArgumentException("User ID is required");
        }

        log.debug("Validation passed for PaymentMethodDTO: {}", dto);
    }
}
