package com.example.ecommerce.controllers.user;

import com.example.ecommerce.dtos.AddressBody;
import com.example.ecommerce.exceptions.InvalidAddressId;
import com.example.ecommerce.model.User;
import com.example.ecommerce.repositories.AddressRepo;
import com.example.ecommerce.services.UserDetailModificationService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserController {
    private UserDetailModificationService userDetailModificationService;

    public UserController(UserDetailModificationService userDetailModificationService, AddressRepo addressRepo) {
        this.userDetailModificationService = userDetailModificationService;
    }

    @GetMapping("/address")
    public ResponseEntity getAddress(@AuthenticationPrincipal User user){
        return ResponseEntity.ok().body(userDetailModificationService.getAddressByUserId(user.getId()));
    }

    @PutMapping("/address/update")
    public ResponseEntity updateAddress(@AuthenticationPrincipal User user,@RequestBody AddressBody addressBody) throws InvalidAddressId {
        return ResponseEntity.ok().body(userDetailModificationService.updateAddress(user.getId(),addressBody));
    }

    @PutMapping("/address/modify/{address_id}")
    public ResponseEntity modifyAddress(@AuthenticationPrincipal User user,@RequestBody AddressBody addressBody,@PathVariable Long address_id) throws InvalidAddressId {
        return ResponseEntity.ok().body(userDetailModificationService.modifyAddress(user.getId(),addressBody,address_id));
    }
}
