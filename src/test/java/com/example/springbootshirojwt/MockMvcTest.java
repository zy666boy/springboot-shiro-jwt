package com.example.springbootshirojwt;

import com.example.springbootshirojwt.controller.TestTestController;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.hamcrest.Matchers.equalTo;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class MockMvcTest {
    private MockMvc mvc;
    //初始化执行
    @Before
    public void setUp() throws Exception{
        mvc= MockMvcBuilders.standaloneSetup(new TestTestController()).build();
    }
    //验证controller是否正常响应并打印返回结果
    @Test
    public void getAll() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/getAll").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andReturn();
    }
    //验证controller是否正常响应并判断返回结果是否正确
    @Test
    public void testHello() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/getAll").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string(equalTo("MockMvcTest success!")));
    }
}
