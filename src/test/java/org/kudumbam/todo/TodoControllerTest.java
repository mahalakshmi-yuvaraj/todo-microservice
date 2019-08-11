package org.kudumbam.todo;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.kudumbam.todo.controller.TodoController;
import org.kudumbam.todo.model.Todo;
import org.kudumbam.todo.service.TodoService;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.hamcrest.CoreMatchers.containsString;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@WebMvcTest(TodoController.class)
public class TodoControllerTest {

 @Autowired
 private MockMvc mvc;

 @MockBean
 private TodoService service;
 
 @Test
 public void retrieveTodos() throws Exception {
  List<Todo> mockList = Arrays.asList(new Todo(1, "Jack",
  "Learn Spring MVC", new Date(), false), new Todo(2, "Jack",
  "Learn Struts", new Date(), false));

  when(service.retrieveTodos()).thenReturn(mockList);

  MvcResult result = mvc
		  .perform(MockMvcRequestBuilders
		  .get("/todos")
		  .accept(MediaType.APPLICATION_JSON))
		  .andExpect(status().isOk())
		  .andReturn();

 String expected = "["
  + "{id:1,user:Jack,description:\"Learn Spring MVC\",done:false}" +","
  + "{id:2,user:Jack,description:\"Learn Struts\",done:false}" + "]";

  JSONAssert.assertEquals(expected, result.getResponse()
   .getContentAsString(), false);
  }

 @Test
 public void retrieveTodosByName() throws Exception {
  List<Todo> mockList = Arrays.asList(new Todo(1, "Jack",
  "Learn Spring MVC", new Date(), false), new Todo(2, "Jack",
  "Learn Struts", new Date(), false));

  when(service.retrieveTodoByName("Jack")).thenReturn(mockList);

  MvcResult result = mvc
		  .perform(MockMvcRequestBuilders
		  .get("/users/Jack/todos")
		  .accept(MediaType.APPLICATION_JSON))
		  .andExpect(status().isOk())
		  .andReturn();

 String expected = "["
  + "{id:1,user:Jack,description:\"Learn Spring MVC\",done:false}" +","
  + "{id:2,user:Jack,description:\"Learn Struts\",done:false}" + "]";

  JSONAssert.assertEquals(expected, result.getResponse()
   .getContentAsString(), false);
  }


@Test
  public void retrieveTodo() throws Exception {
    Todo mockTodo = new Todo(1, "Jack", "Learn Spring MVC", 
    new Date(), false);

    when(service.retrieveTodoById(1)).thenReturn(mockTodo);

    MvcResult result = mvc.perform(
    MockMvcRequestBuilders.get("/users/Jack/todos/1")
    .accept(MediaType.APPLICATION_JSON))
    .andExpect(status().isOk()).andReturn();

    String expected = "{id:1,user:Jack,description:\"Learn Spring MVC\",done:false}";

   JSONAssert.assertEquals(expected, 
    result.getResponse().getContentAsString(), false);

  }


@Test
 public void createTodo() throws Exception {
  Todo mockTodo = new Todo(1, "Jack", 
  "Learn Spring MVC", new Date(), false);
  String todo = "{\"user\":\"Jack\",\"description\":\"Learn Spring MVC\",\"targetDate\":\"2019-08-08T12:29:42.000+0000\",\"done\":false}";

 when(service.addTodo(mockTodo))
 .thenReturn(mockTodo);

mvc
 .perform(MockMvcRequestBuilders.post("/todos")
 .content(todo)
 .contentType(MediaType.APPLICATION_JSON)
 )
 .andExpect(status().isNoContent());
}

}
