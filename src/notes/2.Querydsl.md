# Spring Data JPA: QueryDSL

## QueryDSL이란?

**Java 코드로 타입 안전하게 쿼리를 작성**할 수 있는 프레임워크.

### Query Method의 한계
```
// 고정된 조건만 가능
findByTitleContaining(String keyword, Pageable pageable)

// 동적 조건 조합이 필요한 경우
→ 제목만 검색 / 제목+작성자 검색 / 날짜 범위 검색
→ 조합마다 메서드를 새로 만들어야 함 ❌
```

→ 동적 쿼리가 필요할 때 **QueryDSL** 사용 (국내 실무에서 가장 많이 쓰임)

---

## 1. build.gradle 설정

```groovy
dependencies {
    // QueryDSL
    implementation 'com.querydsl:querydsl-jpa:5.0.0:jakarta'
    annotationProcessor 'com.querydsl:querydsl-apt:5.0.0:jakarta'

    // Jakarta
    annotationProcessor 'jakarta.annotation:jakarta.annotation-api'
    annotationProcessor 'jakarta.persistence:jakarta.persistence-api'
}
```

---

## 2. Q-Domain 클래스 생성

설정 후 터미널에서 컴파일 실행:

```bash
./gradlew compileJava
```

실행하면 `build/generated/` 경로에 **Q-Domain 클래스 자동 생성**됨.

```
build/
  generated/
    annotationProcessor/
      main/
        QTodo.java  ← 자동 생성, 절대 직접 수정 ❌
```

> 엔티티 변경 시 `compileJava` 다시 실행하면 자동 갱신됨

---

## 3. QuerydslConfig 설정

```java
package com.example.config;

import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class QuerydslConfig {

    @PersistenceContext
    private EntityManager entityManager;

    @Bean
    public JPAQueryFactory jpaQueryFactory() {
        return new JPAQueryFactory(entityManager);
    }
}
```

---

## 4. 사용 예시

```java
@Autowired
JPAQueryFactory queryFactory;

@Test
public void testSearch() {
    QTodo todo = QTodo.todo;

    List<Todo> result = queryFactory
        .selectFrom(todo)
        .where(todo.title.contains("11")) // 동적 조건
        .fetch();

    result.forEach(System.out::println);
}
```

### 생성되는 SQL

```sql
SELECT * FROM tbl_todo
WHERE title LIKE '%11%';
```

---

## Query Method vs QueryDSL 비교

| | Query Method | QueryDSL |
|---|---|---|
| 쿼리 작성 방식 | 메서드 이름 | Java 코드 |
| 타입 안전성 | ❌ 문자열 기반 | ✅ 컴파일 타임 체크 |
| 동적 쿼리 | ❌ 불가 | ✅ 가능 |
| 난이도 | 쉬움 | 보통 |
| 적합한 경우 | 단순 고정 조건 | 복잡한 동적 조건 |

---

## 참고

- QueryDSL은 **선택 학습** — 리액트 연동 강의에서는 필수 아님
- 세팅 오류 시 강의 자료의 프로젝트 파일 사용
- 버전마다 세팅 방법이 다를 수 있음 → **5.0 기준** 사용 권장