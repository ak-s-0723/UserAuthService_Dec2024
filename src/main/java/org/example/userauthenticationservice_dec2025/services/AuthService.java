package org.example.userauthenticationservice_dec2025.services;

import org.example.userauthenticationservice_dec2025.exceptions.PasswordMismatchException;
import org.example.userauthenticationservice_dec2025.exceptions.UserAlreadyExistException;
import org.example.userauthenticationservice_dec2025.exceptions.UserNotRegisteredException;
import org.example.userauthenticationservice_dec2025.models.Role;
import org.example.userauthenticationservice_dec2025.models.User;
import org.example.userauthenticationservice_dec2025.repos.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;


@Service
public class AuthService implements IAuthService {

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    public User signup(String email, String password) throws UserAlreadyExistException {
        Optional<User> userOptional = userRepo.findByEmail(email);
        if(userOptional.isPresent()) {
          throw new UserAlreadyExistException("Please try logging....");
        }

        User user = new User();
        user.setEmail(email);
        user.setPassword(bCryptPasswordEncoder.encode(password));
        user.setCreatedAt(new Date());
        user.setLastUpdatedAt(new Date());
        Role role = new Role();
        role.setValue("CUSTOMER");

        //Just for happiness
        List<Role> roles = new ArrayList<>();
        roles.add(role);
        user.setRoles(roles);

        userRepo.save(user);
        return user;
    }

    @Override
    public User login(String email, String password) throws UserNotRegisteredException, PasswordMismatchException {
        Optional<User> userOptional = userRepo.findByEmail(email);
        if(userOptional.isEmpty()) {
           throw new UserNotRegisteredException("Please signup first");
        }

        String storedPassword = userOptional.get().getPassword();
        if(!bCryptPasswordEncoder.matches(password,storedPassword)) {
        //if(!password.equals(storedPassword)) {
          throw new PasswordMismatchException("Please add correct password");
        }

        return userOptional.get();
    }
}
