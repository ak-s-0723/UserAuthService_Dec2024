package org.example.userauthenticationservice_dec2025.controllers;

import org.example.userauthenticationservice_dec2025.dtos.UserDto;
import org.example.userauthenticationservice_dec2025.models.User;
import org.example.userauthenticationservice_dec2025.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/{id}")
    public UserDto getUserById(@PathVariable Long id) {
        User user = userService.getUserDetails(id);
        if(user == null) return null;
        return from(user);
    }

    public UserDto from(User user) {
        UserDto userDto = new UserDto();
        userDto.setId(user.getId());
        userDto.setEmail(user.getEmail());
       // userDto.setRoles(user.getRoles());
        return userDto;
    }
}
