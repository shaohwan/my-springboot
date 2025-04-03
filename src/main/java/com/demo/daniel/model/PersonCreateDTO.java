package com.demo.daniel.model;

import lombok.Data;

@Data
public class PersonCreateDTO {

    private String name;

    private String password;

    private String email;

    private String phone;
}
