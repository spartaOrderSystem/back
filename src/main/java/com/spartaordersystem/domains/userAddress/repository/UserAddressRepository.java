package com.spartaordersystem.domains.userAddress.repository;

import com.spartaordersystem.domains.userAddress.entity.UserAddress;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface UserAddressRepository extends JpaRepository<UserAddress, UUID> {
}
