package com.example.springbootshirojwt.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class UserInfo implements Serializable {
    private String userName;
    private String name;
    private String imgPath;
}
