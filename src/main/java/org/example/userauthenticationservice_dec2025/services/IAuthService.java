package org.example.userauthenticationservice_dec2025.services;

import org.example.userauthenticationservice_dec2025.exceptions.PasswordMismatchException;
import org.example.userauthenticationservice_dec2025.exceptions.UserAlreadyExistException;
import org.example.userauthenticationservice_dec2025.exceptions.UserNotRegisteredException;
import org.example.userauthenticationservice_dec2025.models.User;

public interface IAuthService {

    User signup(String email,String password) throws UserAlreadyExistException;

    User login(String email, String password) throws UserNotRegisteredException, PasswordMismatchException;
}
