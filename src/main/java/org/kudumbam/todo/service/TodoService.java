package org.kudumbam.todo.service;

import java.util.List;
import java.util.Optional;

import org.kudumbam.todo.model.Todo;
import org.kudumbam.todo.repository.TodoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TodoService {
	@Autowired
	private TodoRepository todoRepo;
	
	public List<Todo> retrieveTodos() {       
		return todoRepo.findAll();
	}
		
	public List<Todo> retrieveTodoByName(String user) {       
		return todoRepo.findByUser(user);
	}
	
	public Todo addTodo(Todo todo) {      
		return todoRepo.save(todo);
		    
	}    
	
	public Todo retrieveTodoById(int id) {      
		Optional<Todo> todoById = todoRepo.findById(id); 
		if(todoById.isPresent()){
				return todoById.get();
		}
		else {
			return null;
		}
	}
}
