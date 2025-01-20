package org.example.userauthenticationservice_dec2025.services;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.MacAlgorithm;
import org.antlr.v4.runtime.misc.Pair;
import org.example.userauthenticationservice_dec2025.exceptions.PasswordMismatchException;
import org.example.userauthenticationservice_dec2025.exceptions.UserAlreadyExistException;
import org.example.userauthenticationservice_dec2025.exceptions.UserNotRegisteredException;
import org.example.userauthenticationservice_dec2025.models.Role;
import org.example.userauthenticationservice_dec2025.models.Session;
import org.example.userauthenticationservice_dec2025.models.Status;
import org.example.userauthenticationservice_dec2025.models.User;
import org.example.userauthenticationservice_dec2025.repos.SessionRepo;
import org.example.userauthenticationservice_dec2025.repos.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.*;


@Service
public class AuthService implements IAuthService {

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;


    @Autowired
    private SessionRepo sessionRepo;


    @Autowired
    private SecretKey secretKey;

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
    public Pair<User,String> login(String email, String password) throws UserNotRegisteredException, PasswordMismatchException {
        Optional<User> userOptional = userRepo.findByEmail(email);
        if(userOptional.isEmpty()) {
           throw new UserNotRegisteredException("Please signup first");
        }

        String storedPassword = userOptional.get().getPassword();
        if(!bCryptPasswordEncoder.matches(password,storedPassword)) {
        //if(!password.equals(storedPassword)) {
          throw new PasswordMismatchException("Please add correct password");
        }

        //Generating JWT
//        String message = "{\n" +
//                "   \"email\": \"anurag@gmail.com\",\n" +
//                "   \"roles\": [\n" +
//                "      \"instructor\",\n" +
//                "      \"buddy\"\n" +
//                "   ],\n" +
//                "   \"expirationDate\": \"2ndApril2025\"\n" +
//                "}";
//
//        byte[] content = message.getBytes(StandardCharsets.UTF_8);
        //String token = Jwts.builder().content(content).compact();

        Map<String,Object> payload = new HashMap<>();
        Long nowInMillis = System.currentTimeMillis();
        payload.put("iat",nowInMillis);
        payload.put("exp",nowInMillis+100000);
        payload.put("userId",userOptional.get().getId());
        payload.put("iss","scaler");
        payload.put("scope",userOptional.get().getRoles());

//        MacAlgorithm algorithm = Jwts.SIG.HS256;
//        SecretKey secretKey = algorithm.key().build();
        String token = Jwts.builder().claims(payload).signWith(secretKey).compact();

        Session session = new Session();
        session.setToken(token);
        session.setUser(userOptional.get());
        session.setStatus(Status.ACTIVE);
        sessionRepo.save(session);

        return new Pair<User,String>(userOptional.get(),token);
    }

    //validateToken(userId, token) {
        // check if token stored in db is matching with this token or not
        // whether the token has expired or not ,
        // currentTimeStamp < expiryTimeStamp
        //In order to get expiryTimeStamp, we need to parse token and get payload(claims)
        // -> get expiry.
    //}

    public Boolean validateToken(String token,Long userId) {
      Optional<Session> optionalSession = sessionRepo.findByTokenAndUser_Id(token,userId);

      if(optionalSession.isEmpty()) {
          return false;
      }

      JwtParser jwtParser = Jwts.parser().verifyWith(secretKey).build();
      Claims claims = jwtParser.parseSignedClaims(token).getPayload();

      Long tokenExpiry = (Long) claims.get("exp");
      Long currentTime = System.currentTimeMillis();

        System.out.println(tokenExpiry);
        System.out.println(currentTime);

      if(currentTime > tokenExpiry) {
          Session session = optionalSession.get();
          session.setStatus(Status.INACTIVE);
          sessionRepo.save(session);
          return false;
      }

      return true;
    }
}
