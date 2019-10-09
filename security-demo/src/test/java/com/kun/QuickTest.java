package com.kun;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.restdocs.JUnitRestDocumentation;
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;

@RunWith(SpringRunner.class)
@SpringBootTest       // Spring Boot 环境
@AutoConfigureMockMvc // 启动自动配置模拟的MVC环境
public class QuickTest {

    @Autowired
    private MockMvc mockMvc;

    @Rule
    public JUnitRestDocumentation restDocumentation = new JUnitRestDocumentation();

    @Test
    public void testBookController() throws Exception {
        //url请求地址
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/quick/user")
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .param("userName", "Tom")
                .param("password", "123");

        //执行请求
        this.mockMvc.perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())            //期待的状态码
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.content().string("quick"))
                .andReturn();
    }

    @Test
    public void testUserController() throws Exception {
        //url请求地址
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/quick/user/1")
                .accept(MediaType.APPLICATION_JSON_UTF8);

        //执行请求
        this.mockMvc.perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().is(200))            //期待的状态码
                .andDo(MockMvcResultHandlers.print())
                .andDo(MockMvcRestDocumentation.document("quick",
                        responseFields(fieldWithPath("userName").description("用户名")),
                        responseFields(fieldWithPath("password").description("密码"))))
                .andExpect(MockMvcResultMatchers.jsonPath("$.userName").value("jack"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.password").value("123"))
                .andReturn();
    }

    @Test
    public void testCreateController() throws Exception {
        //url请求地址
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post("/quick/user")
                .param("userName", "Tom")
                .accept(MediaType.APPLICATION_JSON_UTF8);

        //执行请求
        this.mockMvc.perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().is(200))            //期待的状态码
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.jsonPath("$.userName").value("jack"))
                .andReturn();
    }

}
