package org.zerock.mallapi.repository;

import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.zerock.mallapi.domain.Todo;

import java.time.LocalDate;
import java.util.Optional;

@SpringBootTest
@Log4j2
public class TodoRepositoryTests {

    @Autowired
    private TodoRepository todoRepository;

    @Test
    public void testInsert() {

        for (int i = 0; i < 100; i++) {
        Todo todo = Todo.builder()
                .title("TestTitle" + i)
                .writer("user" + i)
                .complete(false)
                .dueDate(LocalDate.now())
                .build();

        Todo result = todoRepository.save(todo);

        log.info("TNO: {}", result.getTno());

        }
    }

    @Test
    public void testRead() {

        Todo savedTodo = todoRepository.save(
                Todo.builder()
                        .title("Read Test")
                        .writer("reader")
                        .complete(false)
                        .dueDate(LocalDate.now())
                        .build()
        );

        Optional<Todo> result = todoRepository.findById(savedTodo.getTno());

        Assertions.assertTrue(result.isPresent());
        Assertions.assertEquals("Read Test", result.orElseThrow().getTitle());
    }

    @Test
    public void testUpdate() {

        Todo savedTodo = todoRepository.save(
                Todo.builder()
                        .title("Before Update")
                        .writer("user01")
                        .complete(false)
                        .dueDate(LocalDate.now())
                        .build()
        );

        Todo todo = todoRepository.findById(savedTodo.getTno()).orElseThrow();
        todo.changeTitle("After Update");
        todo.changeWriter("user02");
        todo.changeComplete(true);
        todo.changeDueDate(LocalDate.now().plusDays(1));

        Todo updatedTodo = todoRepository.save(todo);

        Assertions.assertEquals("After Update", updatedTodo.getTitle());
        Assertions.assertEquals("user02", updatedTodo.getWriter());
        Assertions.assertTrue(updatedTodo.isComplete());
    }

    @Test
    public void testDelete() {

        Todo savedTodo = todoRepository.save(
                Todo.builder()
                        .title("Delete Test")
                        .writer("deleter")
                        .complete(false)
                        .dueDate(LocalDate.now())
                        .build()
        );

        todoRepository.deleteById(savedTodo.getTno());

        Optional<Todo> result = todoRepository.findById(savedTodo.getTno());

        Assertions.assertFalse(result.isPresent());
    }

    @Test
    public void testPaging() {
        Pageable pageable = PageRequest.of(0, 10, Sort.by("tno").descending());

        Page<Todo> result = todoRepository.findAll(pageable);

        log.info(result.getTotalPages());
        log.info(result.getTotalElements());
        /*
        *
           todoRepository.findAll(pageable).forEach(todo -> {
               log.info(todo);
            });
        *
        */
    }

    @Test
    public void testSearch1() {
        Pageable pageable = PageRequest.of(0,10,Sort.by("tno").descending());

        Page<Todo> result = todoRepository.findByTitleContaining("1", pageable);
        System.out.println("test Start -----");
        result.stream().forEach(log::info);
        System.out.println("test End -----");
    }


    @Test
    public void testSearch1_1() {
        Pageable pageable = PageRequest.of(0, 10, Sort.by("tno").ascending());

        Page<Todo> result = todoRepository.findAll(pageable);  // title 조건 없음
        System.out.println("test Start -----");
        result.stream().forEach(log::info);
        System.out.println("test End -----");
    }


}
