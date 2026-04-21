package org.zerock.mallapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.zerock.mallapi.domain.Todo;

// <entity, PK type> 지정
public interface TodoRepository extends JpaRepository<Todo, Long> {
    // test > MallapiApplicationTests
}
