package com.example.book.domain;

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
}
