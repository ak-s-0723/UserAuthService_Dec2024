package org.example.userauthenticationservice_dec2025.services;

import org.antlr.v4.runtime.misc.Pair;
import org.example.userauthenticationservice_dec2025.exceptions.PasswordMismatchException;
import org.example.userauthenticationservice_dec2025.exceptions.UserAlreadyExistException;
import org.example.userauthenticationservice_dec2025.exceptions.UserNotRegisteredException;
import org.example.userauthenticationservice_dec2025.models.User;

public interface IAuthService {

    User signup(String email,String password) throws UserAlreadyExistException;

    Pair<User,String> login(String email, String password) throws UserNotRegisteredException, PasswordMismatchException;

    Boolean validateToken(String token,Long userId);
}
