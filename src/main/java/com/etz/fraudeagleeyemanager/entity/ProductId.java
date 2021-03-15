package com.etz.fraudeagleeyemanager.entity;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.io.*;

@Data
public class ProductId implements Serializable {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Product Code cannot be empty")
    @Column(name = "code")
    private String code;
}
