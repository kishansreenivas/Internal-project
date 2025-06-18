package com.UserService.Repositories;


import org.springframework.data.jpa.repository.JpaRepository;

import com.UserService.Entity.PaymentMethod;

import java.util.UUID;

public interface PaymentMethodRepository extends JpaRepository<PaymentMethod, UUID> {
}
