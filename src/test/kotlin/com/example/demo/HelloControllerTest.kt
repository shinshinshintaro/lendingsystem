package com.example.demo

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status


@ExtendWith(SpringExtension::class)
@WebMvcTest(controllers = [HelloController::class])
class HelloControllerTest {

 @Autowired
 private lateinit var mockMvc: MockMvc

 @Test
 fun getHello() {
  mockMvc.perform(get("/hello"))
   .andExpect(status().isOk)
   .andExpect(content().string("Hello World!"))
 }

 @Test
 fun 存在しないエンドポイントではNotFoundが出る() {
  mockMvc.perform(get("/helloworld"))  // 存在しないエンドポイント
   .andExpect(status().isNotFound)
 }

}
