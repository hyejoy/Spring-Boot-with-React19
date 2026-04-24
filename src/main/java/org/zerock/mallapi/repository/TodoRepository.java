package org.zerock.mallapi.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.zerock.mallapi.domain.Todo;

// <entity, PK type> 지정
// JpaRepository 확장을 통해 CRUD 전부 가능해짐
public interface TodoRepository extends JpaRepository<Todo, Long> {
    // test > MallapiApplicationTests

    // title로 검색하는 페이징 처리
    // Pageable 있으면 무조건 return type Page<Generic>
    Page<Todo> findByTitleContaining(String title, Pageable pageable);

}
