package com.demo.daniel.model;

import lombok.Data;

@Data
public class PersonRequest {

    private String name;

    private String password;

    private String email;

    private String phone;
}
