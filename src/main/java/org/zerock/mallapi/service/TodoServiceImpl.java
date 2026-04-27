package org.zerock.mallapi.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.zerock.mallapi.domain.Todo;
import org.zerock.mallapi.dto.PageReqeusetDTO;
import org.zerock.mallapi.dto.PageResponseDTO;
import org.zerock.mallapi.dto.TodoDTO;
import org.zerock.mallapi.repository.TodoRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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

    @Override
    public void modify(TodoDTO todoDTO) {
        //Todo 엔티티 조회
        Optional<Todo> result = todoRepository.findById(todoDTO.getTno());
        Todo todo = result.orElseThrow();

        // title, complete, dueDate
        todo.changeTitle(todoDTO.getTitle());
        todo.changeComplete(todoDTO.isComplete());
        todo.changeDueDate(todoDTO.getDueDate());

        // dirty checking
        // entity 변경 -> 자동으로 변경시키는거 확인

    }

    @Override
    public void remove(long tno) {

        log.info("......remove.....");
        //todoRepository.deleteById(tno);
    }

    @Override
    public PageResponseDTO<TodoDTO> list(PageReqeusetDTO pageReqeusetDTO) {
        // pageable 생성
        Pageable pageable = PageRequest.of(
                pageReqeusetDTO.getPage() -1,
                pageReqeusetDTO.getSize(),
                Sort.by("tno").descending());

        // todoRepostiory 호출
        Page<Todo> result = todoRepository.findAll(pageable);
        // 결과를 PageResponseDTO로 처리

        List<TodoDTO> dtoList = result.getContent().stream()
                .map(todo -> modelMapper.map(todo, TodoDTO.class))
                .collect(Collectors.toList());

        long totalCOunt = result.getTotalElements();

        PageResponseDTO<TodoDTO> responseDTO=
                PageResponseDTO.<TodoDTO>withAll()
                        .dtoList(dtoList)
                        .pageReqeusetDTO(pageReqeusetDTO)
                        .totalCount(totalCOunt)
                        .build();
        return responseDTO;


    }


}
