package org.example.userauthenticationservice_dec2025.services;

import org.example.userauthenticationservice_dec2025.models.User;
import org.example.userauthenticationservice_dec2025.repos.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {
    @Autowired
    private UserRepo userRepo;

    public User getUserDetails(Long id) {
        Optional<User> optionalUser = userRepo.findById(id);
        if(optionalUser.isEmpty()) return null;
        return optionalUser.get();
    }
}
