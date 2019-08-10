package org.kudumbam.todo.repository;

import java.util.List;

import org.kudumbam.todo.model.Todo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TodoRepository extends JpaRepository<Todo, Integer>{

	List<Todo> findByUser(String user);

}
