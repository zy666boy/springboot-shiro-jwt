package com.example.springbootshirojwt.model;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
@Data
public class TestTest implements Serializable {
    private Integer test_id;
    private String testName;
}
