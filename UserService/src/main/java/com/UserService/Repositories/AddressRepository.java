package com.UserService.Repositories;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.UserService.Entity.Address;

@Repository
public interface AddressRepository extends JpaRepository<Address, Long> {}