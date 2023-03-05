package com.example.book.domain;

import org.springframework.data.jpa.repository.JpaRepository;

// @Repository 를 기재해야 스프링 IoC에 빈으로 등록이 되지만
// JpaRepository를 상속받으면 생략이 가능함
// JpaRepository -> CRUD 함수를 들고 있음
public interface BookRepository extends JpaRepository<Book, Long>{

}
