package org.zerock.mallapi.service;

import org.zerock.mallapi.dto.PageReqeusetDTO;
import org.zerock.mallapi.dto.PageResponseDTO;
import org.zerock.mallapi.dto.TodoDTO;

public interface TodoService {
    Long register(TodoDTO todoDTO);           // 등록
    TodoDTO get(Long tno);                    // 단건 조회
    // return 타입보다는 예외로 설계
    void modify(TodoDTO todoDTO);             // 수정
    void remove(long tno);                    // 삭제 (Sofre Delete 권장

    PageResponseDTO<TodoDTO> list(PageReqeusetDTO pageReqeusetDTO); // 목록
}
