package com.example.springbootshirojwt.model;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("test_test")
public class TestTest {
    @TableId
    private Integer test_id;
    private String testName;
}
