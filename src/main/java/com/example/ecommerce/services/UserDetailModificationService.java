package com.example.ecommerce.services;

import com.example.ecommerce.dtos.AddressBody;
import com.example.ecommerce.exceptions.InvalidAddressId;
import com.example.ecommerce.model.Address;
import com.example.ecommerce.repositories.AddressRepo;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserDetailModificationService {
    private AddressRepo addressRepo;

    public UserDetailModificationService(AddressRepo addressRepo) {
        this.addressRepo = addressRepo;
    }

    //Get all the address
    public List<Address> getAddressByUserId(Long id){
        return addressRepo.findByUser_Id(id);
    }

    //Add or update address
    public String updateAddress(Long id, AddressBody addressBody) throws InvalidAddressId{
        Optional<Address> opAddress = addressRepo.findByUser_IdAndAddressId(id,addressBody.getAddressId());
        if(opAddress.isEmpty()){
            throw new InvalidAddressId("Invalid Address Id");
        }

        Address address = opAddress.get();
        address.setAddressLine(addressBody.getAddressLine());
        address.setCity(addressBody.getCity());
        address.setCountry(addressBody.getCountry());
        address.setPostalCode(addressBody.getPostalCode());

        addressRepo.save(address);
        return "Address updated.";
    }

}
