package org.example.userauthenticationservice_dec2025.dtos;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class EmailDto {
    String to;
    String from;
    String subject;
    String body;
}
