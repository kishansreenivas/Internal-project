package com.UserService.Mapper;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.UserService.Dto.AddressDTO;
import com.UserService.Dto.PaymentMethodDTO;
import com.UserService.Dto.PreferencesDTO;
import com.UserService.Dto.UserDTO;
import com.UserService.Entity.Address;
import com.UserService.Entity.PaymentMethod;
import com.UserService.Entity.Preferences;
import com.UserService.Entity.User;

@Component
public class UserMapper {
    public UserDTO toDTO(User user) {
        UserDTO dto = new UserDTO();
        dto.setId(user.getId());
        dto.setFirstName(user.getFirstName());
        dto.setLastName(user.getLastName());
        dto.setEmail(user.getEmail());
        dto.setPhone(user.getPhone());

        dto.setAddresses(user.getAddresses().stream().map(addr -> {
            AddressDTO adto = new AddressDTO();
            adto.setId(addr.getId());
            adto.setStreet(addr.getStreet());
            adto.setCity(addr.getCity());
            adto.setState(addr.getState());
            adto.setPostalCode(addr.getPostalCode());
            return adto;
        }).collect(Collectors.toList()));

        if (user.getPreferences() != null) {
            PreferencesDTO pdto = new PreferencesDTO();
            pdto.setNewsletterSubscribed(user.getPreferences().isNewsletterSubscribed());
            pdto.setLanguage(user.getPreferences().getLanguage());
            pdto.setRegion(user.getPreferences().getRegion());
            dto.setPreferences(pdto);
        }

        dto.setPaymentMethods(user.getPaymentMethods().stream().map(pm -> {
            PaymentMethodDTO pmdto = new PaymentMethodDTO();
            pmdto.setId(pm.getId());
            pmdto.setCardNumber(pm.getCardNumber());
            pmdto.setCardHolder(pm.getCardHolder());
            pmdto.setExpiry(pm.getExpiry());
            pmdto.setType(pm.getType());
            return pmdto;
        }).collect(Collectors.toList()));

        dto.setWatchlistMovieIds(user.getWatchlistMovieIds());
        dto.setBookingId(user.getBookingId());
        return dto;
    }

    public User toEntity(UserDTO dto) {
        User user = new User();
        updateEntityFromDto(dto, user);
        return user;
    }

    public void updateEntityFromDto(UserDTO dto, User user) {
        user.setFirstName(dto.getFirstName());
        user.setLastName(dto.getLastName());
        user.setEmail(dto.getEmail());
        user.setPhone(dto.getPhone());

        if (dto.getAddresses() != null) {
            user.getAddresses().clear();
            List<Address> addresses = dto.getAddresses().stream().map(adto -> {
                Address addr = new Address();
                addr.setId(adto.getId());
                addr.setStreet(adto.getStreet());
                addr.setCity(adto.getCity());
                addr.setState(adto.getState());
                addr.setPostalCode(adto.getPostalCode());
                addr.setUser(user);
                return addr;
            }).collect(Collectors.toList());
            user.getAddresses().addAll(addresses);
        }

        if (dto.getPreferences() != null) {
            Preferences prefs = new Preferences();
            prefs.setNewsletterSubscribed(dto.getPreferences().isNewsletterSubscribed());
            prefs.setLanguage(dto.getPreferences().getLanguage());
            prefs.setRegion(dto.getPreferences().getRegion());
            prefs.setUser(user);
            user.setPreferences(prefs);
        }

        if (dto.getPaymentMethods() != null) {
            user.getPaymentMethods().clear();
            List<PaymentMethod> paymentMethods = dto.getPaymentMethods().stream().map(pmdto -> {
                PaymentMethod pm = new PaymentMethod();
                pm.setId(pmdto.getId());
                pm.setCardNumber(pmdto.getCardNumber());
                pm.setCardHolder(pmdto.getCardHolder());
                pm.setExpiry(pmdto.getExpiry());
                pm.setType(pmdto.getType());
                pm.setUser(user);
                return pm;
            }).collect(Collectors.toList());
            user.getPaymentMethods().addAll(paymentMethods);
        }

        user.setWatchlistMovieIds(dto.getWatchlistMovieIds() != null ? dto.getWatchlistMovieIds() : new ArrayList<>());
        user.setBookingId(dto.getBookingId() != null ? dto.getBookingId() : new ArrayList<>());
    }
}