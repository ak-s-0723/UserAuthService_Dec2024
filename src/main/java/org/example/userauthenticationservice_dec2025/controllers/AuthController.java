package org.example.userauthenticationservice_dec2025.controllers;

import org.example.userauthenticationservice_dec2025.dtos.LoginRequest;
import org.example.userauthenticationservice_dec2025.dtos.SignupRequest;
import org.example.userauthenticationservice_dec2025.dtos.UserDto;
import org.example.userauthenticationservice_dec2025.exceptions.PasswordMismatchException;
import org.example.userauthenticationservice_dec2025.exceptions.UserAlreadyExistException;
import org.example.userauthenticationservice_dec2025.exceptions.UserNotRegisteredException;
import org.example.userauthenticationservice_dec2025.models.User;
import org.example.userauthenticationservice_dec2025.services.IAuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private IAuthService authService;

    @PostMapping("/signup")
    public ResponseEntity<UserDto> signup(@RequestBody SignupRequest signupRequest) {
        try {
            User user = authService.signup(signupRequest.getEmail(), signupRequest.getPassword());
            return new ResponseEntity<>(from(user),HttpStatus.CREATED);
        }catch (UserAlreadyExistException exception) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }

    }

    @PostMapping("/login")
    public ResponseEntity<UserDto> login(@RequestBody LoginRequest loginRequest) {
      try {
          User user = authService.login(loginRequest.getEmail(), loginRequest.getPassword());
          return new ResponseEntity<>(from(user),HttpStatus.OK);
      }  catch (UserNotRegisteredException exception) {
          return new ResponseEntity<>(null,HttpStatus.NOT_FOUND);
      }  catch (PasswordMismatchException exception) {
          return new ResponseEntity<>(null,HttpStatus.BAD_REQUEST);
      }
    }

    public UserDto from(User user) {
        UserDto userDto = new UserDto();
        userDto.setId(user.getId());
        userDto.setEmail(user.getEmail());
        userDto.setRoles(user.getRoles());
        return userDto;
    }

}
