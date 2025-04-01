package com.demo.daniel.entity;

import jakarta.persistence.*;
import lombok.*;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "person")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Person extends BaseEntity{

    @Column(name = "name")
    private String name;

    @Column(name = "password")
    private String password;

    @Column(name = "email")
    private String email;

    @Column(name = "phone")
    private String phone;
}
