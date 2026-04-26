package org.zerock.mallapi.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.zerock.mallapi.domain.Todo;
import org.zerock.mallapi.dto.TodoDTO;
import org.zerock.mallapi.repository.TodoRepository;

import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
@Log4j2
public class TodoServiceImpl implements TodoService {

    // TotoRepository
    private final TodoRepository todoRepository;
    private final ModelMapper modelMapper;

    @Override
    public Long register(TodoDTO todoDTO) {
        log.info(".......");
        Todo todo = modelMapper.map(todoDTO, Todo.class);
        Todo saveTodo = todoRepository.save(todo);
        return saveTodo.getTno();
    }

    @Override
    public TodoDTO get(Long tno) {
        Optional<Todo> result = todoRepository.findById(tno);
        Todo todo = result.orElseThrow();
        TodoDTO dto = modelMapper.map(todo, TodoDTO.class);
        return dto;
    }


}
