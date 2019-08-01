package com.example.springbootshirojwt.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.springbootshirojwt.model.SysUser;
import com.example.springbootshirojwt.model.TestTest;
import org.apache.ibatis.annotations.Select;

public interface TestTestMapper extends BaseMapper<TestTest> {
    @Select("select test_id,test_name from test_test where test_id=#{id}")
    TestTest getOne(Integer id);
}
