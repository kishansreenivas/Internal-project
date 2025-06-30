package com.UserService.Repositories;


import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.UserService.Entity.PaymentMethod;

public interface PaymentMethodRepository extends JpaRepository<PaymentMethod, UUID> {
}
