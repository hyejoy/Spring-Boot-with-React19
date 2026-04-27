package org.zerock.mallapi.service;

import org.zerock.mallapi.dto.TodoDTO;

public interface TodoService {
    Long register(TodoDTO todoDTO);

    TodoDTO get(Long tno);


    // return 타입보다는 예외로 설계
    void modify(TodoDTO todoDTO);
    void remove(long tno);
}
