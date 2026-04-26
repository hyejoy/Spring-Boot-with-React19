package org.zerock.mallapi.service;

import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.zerock.mallapi.domain.Todo;
import org.zerock.mallapi.dto.TodoDTO;
import org.zerock.mallapi.repository.TodoRepository;

import java.time.LocalDate;
import java.util.Optional;

@SpringBootTest
@Log4j2
public class TodoServiceTest {

    @Autowired
    private TodoService todoService;

    @Autowired
    private TodoRepository todoRepository;

    @Disabled
    @Test
    public void testRegister() {
        TodoDTO todoDTO = TodoDTO.builder()
                .title("Service Test")
                .writer("tester")
                .complete(false)
                .dueDate(LocalDate.now())
                .build();

        Long tno = todoService.register(todoDTO);

        log.info("TNO: {}", tno);

        Assertions.assertNotNull(tno);

        Optional<Todo> result = todoRepository.findById(tno);

        Assertions.assertTrue(result.isPresent());

        Todo todo = result.orElseThrow();

        Assertions.assertEquals(todoDTO.getTitle(), todo.getTitle());
        Assertions.assertEquals(todoDTO.getWriter(), todo.getWriter());
        Assertions.assertEquals(todoDTO.isComplete(), todo.isComplete());
        Assertions.assertEquals(todoDTO.getDueDate(), todo.getDueDate());
    }

    @Test
    public void testReade() {
        Long tno = 1L;
        TodoDTO todoDTO = todoService.get(tno);
        log.info(todoDTO);
    }
}
