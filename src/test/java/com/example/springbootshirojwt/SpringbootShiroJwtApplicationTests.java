package com.example.springbootshirojwt;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SpringbootShiroJwtApplicationTests {

    @Test
    public void contextLoads() {
    }
    @Test
    public void testForEach(){
        List<String> list=new ArrayList();
       list.add("a");
        list.forEach(System.out::println);
    }
    public int  testThrow(int i){
       return 10/i;
    }
    @Test
    public void testThrow2(){
        try{
        testThrow(0);
        }
        catch(Exception e){
           System.out.println("123");
        }
    }

}
