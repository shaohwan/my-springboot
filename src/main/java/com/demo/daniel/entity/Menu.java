package com.demo.daniel.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "sys_menu")
@Data
public class Menu {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "parent_id")
    private Long parentId;

    @Column(name = "name")
    private String name;

    @Column(name = "path")
    private String path;

    @Column(name = "component")
    private String component;

    @Column(name = "icon")
    private String icon;

    @Column(name = "sort")
    private Integer sort;
}
