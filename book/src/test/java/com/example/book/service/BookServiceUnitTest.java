package com.example.book.service;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import com.example.book.domain.BookRepository;

import jakarta.inject.Inject;

/**
 * // 단위 테스트 -> 서비스 안에서 필요한 것만 IoC에 -> bookRepository만 
 *  bookRepository를 Bean에 띄우면 데이터 베이스와 같이 테스트하는것이기에
 *  가짜 객체로 만들기 -> MockitoExtension 환경을 지원해줌
 * @author User
 *
 */


@ExtendWith(MockitoExtension.class) // 스프링 환경으로
public class BookServiceUnitTest {
	
	@InjectMocks // BookService 객체가 만들어질 때
	//BookServiceUnitTest 파일에 @Mock으로 등록된 모든 것들을 주입받음
	private BookService bookService;
	@Mock // mock 환경에 뜨는것 -> IoC x
	private BookRepository bookRepository;

}
