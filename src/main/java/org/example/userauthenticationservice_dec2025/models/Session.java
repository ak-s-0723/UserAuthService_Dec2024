package org.example.userauthenticationservice_dec2025.models;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;

@Entity
@Setter
@Getter
public class Session extends BaseModel {
    private String token;

    @ManyToOne
    private User user;

}

//1         m
//user     session
//1         1
//
//
//1        m