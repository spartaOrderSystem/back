package com.spartaordersystem.domains.UserAddress.repository;

import com.spartaordersystem.domains.UserAddress.entity.UserAddress;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface UserAddressRepository extends JpaRepository<UserAddress, UUID> {
}
