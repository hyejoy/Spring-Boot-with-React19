package org.zerock.mallapi.repository.search;

import org.zerock.mallapi.dto.PageReqeusetDTO;
import org.zerock.mallapi.dto.PageResponseDTO;
import org.zerock.mallapi.dto.TodoDTO;

public interface TodoSearch {
    PageResponseDTO<TodoDTO> search(String keyword, PageReqeusetDTO pageRequestDto);
}
