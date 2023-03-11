package com.example.book.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.transaction.annotation.Transactional;

// 단위 테스트 -> DB 관련 된 Bean이 IoC에 등록되면 됨

@Transactional // 롤백을 위함
																		//Replace.NONE -> 실제 DB로 테스트
@AutoConfigureTestDatabase(replace = Replace.ANY) // 가짜 DB로 테스트
@DataJpaTest // jpa 관련만 메모리에 -> Spring 환경을 갖고 있음(IoC)
public class BookRepositoryUnitTest {

		@Autowired
		private BookRepository bookRepository;
		
		//실제 DB환경을 들고 왔음 
		@Test
		public void save_테스트() {
			//given
			Book book = new Book(null, "책제목1", "책저자1");
			
			//when
			Book bookEntity = bookRepository.save(book);
			
			//then
			// 기대 값과 실제 db에 들어간 타이틀 값과 맞는지 테스트 -> 실제 레파지토리 값만
			assertEquals("책제목1", bookEntity.getTitle());
		}
}