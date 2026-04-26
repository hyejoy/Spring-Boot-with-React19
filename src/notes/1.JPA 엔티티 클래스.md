# Spring Data JPA: 엔티티 클래스 & Repository

## ORM / JPA 개념

| 용어 | 설명 |
|------|------|
| **ORM** | 객체지향(Java) ↔ 관계형(DB) 패러다임을 매핑해주는 기술 |
| **JPA** | Java 진영의 ORM 스펙 |
| **Hibernate** | JPA의 실제 구현체 (Spring Data JPA 사용 시 기본으로 동작) |

```
Java (객체지향)  ←→  ORM (매핑)  ←→  RDB (관계형)
클래스/인스턴스       JPA/Hibernate      테이블/레코드
```

---

## 엔티티 vs 값 객체

DB에 저장하는 객체는 두 종류로 구분됨.

| | 엔티티 | 값 객체 |
|---|---|---|
| 식별자(PK) | ✅ 있음 | ❌ 없음 |
| 예시 | `Todo`, `Member` | `Address`, `Money` |

> 엔티티는 반드시 `@Id` (PK) 지정 필수

---

## 엔티티 클래스 작성

```java
package com.example.domain;

import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Table(name = "tbl_todo")
@Getter  // setter 대신 change 메서드 사용 권장
public class Todo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // PK 자동 증가
    private Long tno;

    private String title;
    private String writer;
    private boolean complete;
    private LocalDate dueDate;

    // setter 대신 change 메서드로 수정
    public void change(String title, boolean complete, LocalDate dueDate) {
        this.title = title;
        this.complete = complete;
        this.dueDate = dueDate;
    }
}
```

### 주요 어노테이션

| 어노테이션 | 설명 |
|-----------|------|
| `@Entity` | JPA 엔티티 클래스 선언 |
| `@Table(name = "tbl_todo")` | 매핑할 DB 테이블명 지정 |
| `@Id` | PK 필드 지정 (필수) |
| `@GeneratedValue` | PK 자동 생성 전략 지정 |

### setter 대신 change 메서드를 쓰는 이유
- 엔티티는 DB와 직접 연결된 객체
- 무분별한 setter 사용 시 어디서든 값이 바뀔 수 있음
- `change()` 같은 명시적 메서드로 변경 의도를 드러냄

---

## 테이블 자동 생성 설정 (application.yml)

```yaml
spring:
  jpa:
    hibernate:
      ddl-auto: update  # 테이블 없으면 자동 생성
```

| 옵션 | 설명 |
|------|------|
| `create` | 실행 시 테이블 새로 생성 (기존 데이터 삭제) |
| `create-drop` | 실행 시 생성, 종료 시 삭제 |
| `update` | 변경사항만 반영, 없으면 생성 |
| `none` | 아무것도 안 함 |

> 개발 시에는 `update`, 운영 시에는 `none` 권장

---

## Repository 인터페이스

```java
package com.example.repository;

import com.example.domain.Todo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TodoRepository extends JpaRepository<Todo, Long> {
    // JpaRepository<엔티티 타입, PK 타입>
    // 기본 CRUD 메서드 자동 제공 — 별도 구현 불필요
}
```

`JpaRepository`가 기본 제공하는 주요 메서드:

| 메서드 | 설명 |
|--------|------|
| `save(entity)` | 저장 / 수정 |
| `findById(id)` | ID로 단건 조회 |
| `findAll()` | 전체 조회 |
| `deleteById(id)` | ID로 삭제 |
| `count()` | 전체 개수 |

---

## DB 클라이언트 툴 추천

| 툴 | 특징 |
|---|---|
| **HeidiSQL** | 윈도우 전용, 가볍고 무난 |
| **DBeaver Community** | 무료 오픈소스, 다양한 DB 지원 |
| **VSCode - Database Client** | VSCode 확장 프로그램, MySQL/MariaDB/PostgreSQL/S3 등 지원 |

---

## 다음 시간

테스트 코드 작성 (`TodoRepository` CRUD 테스트)