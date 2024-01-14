package com.example.ecommerce.repositories;

import com.example.ecommerce.model.Address;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AddressRepo extends JpaRepository<Address,Long> {
    List<Address> findByUser_Id(Long id);

    Optional<Address> findByUser_IdAndAddressId(Long id, Long addressId);

}
