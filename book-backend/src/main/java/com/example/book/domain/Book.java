package com.example.book.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity // 서버 실행시에 테이블이 h2에 생성됨 => Object Relation Mapping이 됨 -> ORM
public class Book {
	@Id // pk를 해당 변수로 지정
	@GeneratedValue(strategy = GenerationType.IDENTITY) // 해당 데이터베이스 번호증가 전략 따라감 -> ex 시퀀스
	private Long id;
	
	private  String title;
	private String author;
	
	// setter만 생성하여 dto를 받는 함수를 만들어 할당해주는 방법이 가장 깔끔함

}
