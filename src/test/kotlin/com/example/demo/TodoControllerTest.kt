package com.example.demo

import com.example.demo.controller.TodoController
import com.example.demo.entity.Todo
import com.example.demo.repository.TodoRepository
import com.fasterxml.jackson.databind.ObjectMapper
import org.junit.jupiter.api.Test
import org.mockito.Mockito.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.http.MediaType
import org.springframework.test.context.bean.override.mockito.MockitoBean
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*
import java.time.OffsetDateTime
import java.util.*

@WebMvcTest(TodoController::class)
class TodoControllerTest {

 @Autowired
 private lateinit var mockMvc: MockMvc

 @MockitoBean
 private lateinit var todoRepository: TodoRepository

 @Test
 fun getTodos() {
  val todos = listOf(
   Todo(
    id = 1L,
    title = "title1",
    description = "description1",
    isCompleted = false,
    createdAt = OffsetDateTime.now(),
    updatedAt = OffsetDateTime.now(),
   ),
   Todo(
    id = 2L,
    title = "title2",
    description = "description2",
    isCompleted = false,
    createdAt = OffsetDateTime.now(),
    updatedAt = OffsetDateTime.now(),
   )
  )

  // Mockの設定
  `when`(todoRepository.findAll()).thenReturn(todos)

  // GET /todos を呼び出して、todosが返ってくることを確認する
  mockMvc.perform(get("/todos"))
   .andExpect(status().isOk)
   .andExpect(content().contentType(MediaType.APPLICATION_JSON))
   .andExpect(jsonPath("$[0].id").value(todos[0].id))
   .andExpect(jsonPath("$[0].title").value(todos[0].title))
   .andExpect(jsonPath("$[0].description").value(todos[0].description))
   .andExpect(jsonPath("$[0].isCompleted").value(todos[0].isCompleted))
   .andExpect(jsonPath("$[1].id").value(todos[1].id))
   .andExpect(jsonPath("$[1].title").value(todos[1].title))
   .andExpect(jsonPath("$[1].description").value(todos[1].description))
   .andExpect(jsonPath("$[1].isCompleted").value(todos[1].isCompleted))

  // todoRepository.findAll()が1回呼び出されたことを確認
  verify(todoRepository, times(1)).findAll()
 }

 @Test
 fun getTodoById() {

  val todo = Todo(
   id = 1L,
   title = "title1",
   description = "description1",
   isCompleted = false,
   createdAt = OffsetDateTime.now(),
   updatedAt = OffsetDateTime.now(),
  )

  // Mockの設定
  `when`(todoRepository.findById(1L)).thenReturn(Optional.of(todo))

  // GET /todo/5 を呼び出して、値が返却されることを確認
  mockMvc.perform(get("/todo/{id}", 1L))
   .andExpect(status().isOk)
   .andExpect(content().contentType(MediaType.APPLICATION_JSON))
   .andExpect(jsonPath("$.id").value(todo.id))
   .andExpect(jsonPath("$.title").value(todo.title))
   .andExpect(jsonPath("$.description").value(todo.description))
   .andExpect(jsonPath("$.isCompleted").value(todo.isCompleted))

  // todoRepository.findById(5)が1回呼び出されたことを確認
  verify(todoRepository, times(1)).findById(1)
 }
 @Test
 fun testPostTodo() {
  val objectMapper = ObjectMapper()

  // 入力されたTodo（リクエストボディ）
  val inputTodo = Todo(
   id = null,
   title = "Test Title",
   description = "Test Description",
   isCompleted = false,
   createdAt = null,
   updatedAt = null
  )

  // 保存されたTodo（レスポンスボディ）
  val savedTodo = Todo(
   id = 1L,
   title = "Test Title",
   description = "Test Description",
   isCompleted = false,
   createdAt = OffsetDateTime.now(),
   updatedAt = OffsetDateTime.now()
  )

  // todoRepository.save()が呼び出されたらsavedTodoを返すように設定
  `when`(todoRepository.save(any())).thenReturn(savedTodo)

  mockMvc.perform(post("/todo", inputTodo)
   .contentType(MediaType.APPLICATION_JSON)
   .content(objectMapper.writeValueAsString(inputTodo)))
   .andExpect(status().isOk)
   .andExpect(content().contentType(MediaType.APPLICATION_JSON))
   .andExpect(jsonPath("$.id").value(savedTodo.id))
   .andExpect(jsonPath("$.title").value(savedTodo.title))
   .andExpect(jsonPath("$.description").value(savedTodo.description))
   .andExpect(jsonPath("$.isCompleted").value(savedTodo.isCompleted))

  verify(todoRepository, times(1)).save(any())
 }
 @Test
 fun deleteTodoById() {
  val todo = Todo(
   id = 1L,
   title = "Test Title",
   description = "Test Description",
   isCompleted = false,
   createdAt = OffsetDateTime.now(),
   updatedAt = OffsetDateTime.now()
  )
  // findById()が返却するOptionalを設定
  `when`(todoRepository.findById(1L)).thenReturn(Optional.of(todo))
  // voidが返却されるメソッドはこう書くらしい https://stackoverflow.com/questions/24006790/mockito-when-thenreturn-for-void-method
  doNothing().`when`(todoRepository).delete(any())

  mockMvc.perform(delete("/todo/{id}", 1L))
   .andExpect(status().isOk)

  // mockのメソッドが呼び出されたことを確認
  verify(todoRepository, times(1)).findById(1L)
  verify(todoRepository, times(1)).delete(any())
 }
}
