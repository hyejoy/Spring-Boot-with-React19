# Spring Boot: Service 계층 & DTO & ModelMapper

## 3티어 아키텍처

```
Controller (웹 계층)
    ↕ DTO (화폐)
Service (서비스 계층) ← 비즈니스 로직, 트랜잭션 처리
    ↕ Entity (금)
Repository (영속 계층) ← DB (금고)
```

| 계층 | 역할 | 데이터 단위 |
|------|------|------------|
| Controller | 요청/응답 처리 | DTO |
| Service | 비즈니스 로직, 트랜잭션 | DTO ↔ Entity 변환 |
| Repository | DB 접근 (JPA) | Entity |

---

## DTO (Data Transfer Object)

- 계층 간 데이터를 **포장해서 전달**하는 객체 (장바구니/화폐)
- 파라미터 타입, 리턴 타입의 단위
- **읽기/쓰기** 모두 가능 (Entity는 읽기 전용 권장)

```java
package com.example.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDate;

@Data                   // getter, setter, toString, equals, hashCode 자동 생성
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TodoDTO {
    private Long tno;
    private String title;
    private String writer;
    private boolean complete;

    @JsonFormat(pattern = "yyyy-MM-dd")  // 날짜 포맷 지정
    private LocalDate dueDate;
}
```

> `@Builder` 사용 시 `@AllArgsConstructor` + `@NoArgsConstructor` 함께 써야 함

---

## Entity vs DTO 비교

| | Entity | DTO |
|---|---|---|
| 역할 | DB 테이블과 1:1 매핑 | 계층 간 데이터 전달 |
| 읽기/쓰기 | 읽기 전용 권장 | 읽기/쓰기 모두 |
| setter | ❌ change() 메서드 사용 | ✅ 자유롭게 사용 |
| 비유 | 금고 속의 금 | 주고받는 화폐 |

---

## Service 계층

### 인터페이스 선언

```java
package com.example.service;

public interface TodoService {
    Long register(TodoDTO todoDTO);
    TodoDTO get(Long tno);
}
```

### 구현체

```java
package com.example.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional          // DB 작업 트랜잭션 처리
@RequiredArgsConstructor
@Log4j2
public class TodoServiceImpl implements TodoService {

    private final TodoRepository todoRepository;
    private final ModelMapper modelMapper;

    // 등록 — DTO → Entity 변환 후 저장
    @Override
    public Long register(TodoDTO todoDTO) {
        Todo todo = modelMapper.map(todoDTO, Todo.class);
        Long tno = todoRepository.save(todo).getTno();
        return tno;
    }

    // 조회 — Entity → DTO 변환 후 반환
    @Override
    public TodoDTO get(Long tno) {
        Optional<Todo> result = todoRepository.findById(tno);
        Todo todo = result.orElseThrow();
        return modelMapper.map(todo, TodoDTO.class);
    }
}
```

### 포인트
- `@Service` → 스프링 빈 등록
- `@Transactional` → 여러 DB 작업을 하나의 트랜잭션으로 묶음
- `@RequiredArgsConstructor` + `private final` → 생성자 주입

---

## ModelMapper — DTO ↔ Entity 변환

간단한 객체 변환 시 사용. 복잡한 객체는 직접 코딩 권장.

### build.gradle 추가

```groovy
implementation 'org.modelmapper:modelmapper:3.2.2'
```

### RootConfig 설정

```java
package com.example.config;

import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RootConfig {

    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration()
            .setMatchingStrategy(MatchingStrategies.STRICT);
        return modelMapper;
    }
}
```

### 변환 방향

```java
// DTO → Entity (등록 시)
Todo todo = modelMapper.map(todoDTO, Todo.class);

// Entity → DTO (조회 시)
TodoDTO dto = modelMapper.map(todo, TodoDTO.class);
```

---

## 테스트 코드

```java
@SpringBootTest
class TodoServiceTests {

    @Autowired
    TodoService todoService;

    @Test
    @Disabled  // 빌드 시 재실행 방지
    public void testRegister() {
        TodoDTO dto = TodoDTO.builder()
            .title("테스트 제목")
            .writer("테스터")
            .dueDate(LocalDate.of(2025, 12, 31))
            .build();

        Long tno = todoService.register(dto);
        log.info("생성된 번호: " + tno);
    }

    @Test
    @Disabled
    public void testGet() {
        TodoDTO dto = todoService.get(1L);
        log.info(dto);
    }
}
```

> 테스트 완료 후 `@Disabled` 추가 — 빌드 과정에서 재실행 방지

---

## 다음 시간

수정 / 삭제 기능 구현