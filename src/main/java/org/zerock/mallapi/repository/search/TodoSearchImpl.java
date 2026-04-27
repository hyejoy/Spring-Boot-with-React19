package org.zerock.mallapi.repository.search;

import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.JPQLQueryFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.zerock.mallapi.domain.QTodo;
import org.zerock.mallapi.domain.Todo;
import org.zerock.mallapi.dto.PageReqeusetDTO;
import org.zerock.mallapi.dto.PageResponseDTO;
import org.zerock.mallapi.dto.TodoDTO;

import java.util.List;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
@Log4j2
public class TodoSearchImpl implements TodoSearch{

    private final JPQLQueryFactory queryFactory;


    @Override
    public PageResponseDTO<TodoDTO> search(String keyword, PageReqeusetDTO pageRequestDto) {
        QTodo todo = QTodo.todo;

        JPQLQuery<Todo> query = queryFactory.selectFrom(todo);

        if (keyword != null && !keyword.isBlank()) {
            query.where(todo.title.contains(keyword));
        }

        long totalCount = query.fetchCount();

        // tno 역순으로 정렬
        query.orderBy(todo.tno.desc());
        // 페이징 처리
        Pageable pageable = PageRequest.of(pageRequestDto.getPage() - 1, pageRequestDto.getSize());
        query.offset(pageable.getOffset());
        query.limit(pageable.getPageSize());

        log.info("---------------------");
        log.info(query);
//        List<TodoDTO> dtoList = query.fetch().stream()
//                .map(todoEntity -> TodoDTO.builder()
//                        .tno(todoEntity.getTno())
//                        .title(todoEntity.getTitle())
//                        .writer(todoEntity.getWriter())
//                        .complete(todoEntity.isComplete())
//                        .dueDate(todoEntity.getDueDate())
//                        .build())
//                .collect(Collectors.toList());
//
//        return PageResponseDTO.<TodoDTO>withAll()
//                .dtoList(dtoList)
//                .pageReqeusetDTO(pageRequestDto)
//                .totalCount(totalCount)
//                .build();
//    }
        return null;
    }
}
