// com.UserService.Controller.AddressController.java
package com.UserService.Controller;

import java.util.List;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.UserService.Dto.AddressDTO;
import com.UserService.Dto.ApiResponse;
import com.UserService.Services.AddressService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/v1/addresses")
@RequiredArgsConstructor
public class AddressController {

    private final AddressService addressService;

    @PostMapping
    public ResponseEntity<ApiResponse<AddressDTO>> create(@RequestBody AddressDTO dto) {
        AddressDTO saved = addressService.createAddress(dto);
        return ResponseEntity.ok(ApiResponse.success(saved));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<AddressDTO>> update(@PathVariable UUID id, @RequestBody AddressDTO dto) {
        AddressDTO updated = addressService.updateAddress(id, dto);
        return ResponseEntity.ok(ApiResponse.success(updated));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<AddressDTO>> getById(@PathVariable UUID id) {
        AddressDTO found = addressService.getAddressById(id);
        return ResponseEntity.ok(ApiResponse.success(found));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<AddressDTO>>> getAll() {
        List<AddressDTO> list = addressService.getAllAddresses();
        return ResponseEntity.ok(ApiResponse.success(list));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<String>> delete(@PathVariable UUID id) {
        addressService.deleteAddress(id);
        return ResponseEntity.ok(ApiResponse.success("Deleted successfully"));
    }
}
