package com.example.book.service;

import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.book.domain.Book;
import com.example.book.domain.BookRepository;

import lombok.RequiredArgsConstructor;

// 기능을 정의할 수 있고, 트랜잭션을 관리할 수 있음

//레파지토리 -> 데이터베이스에 데이터를 넣거나 가져올 때
//서비스 -> 가져오고 다른 곳에서도 가져오고 쿼리를 실행하면서 기능을 구현
//함수 -> 송금() -> 레파지토리에 함수 실행 -> commit or rollback


@RequiredArgsConstructor //final이 붙은 것을 자동으로 생성자로 만들어줌
@Service
public class BookService {

	private final BookRepository bookRepository;
	
	@Transactional // org.springframework.transaction
	// 서비스 함수가 종료될 때 commit을 할지 rollback을 할지 트랜잭션 관리
	public Book 저장하기(Book book) {
		return bookRepository.save(book); 
		// save 한 타입을 다시 리턴해줌 
	}
	
	@Transactional(readOnly = true) // JPA 변경감지하는 내부 기능이을 활성화 x 
	// update시에 정합성을 유지 -> insert시에 팬텀리드 현상 막는것은 불가능
	public Book 한건가져오기(Long id) {
		return bookRepository.findById(id) // 못 찾을 수 있기에 익셉션 처리해줘야함
				.orElseThrow(()-> new IllegalArgumentException("id를 확인하세요"));
		// 메서드가 하나여서 람다식으로 처리함			
	}
	
	@Transactional(readOnly = true)
	public List<Book> 모두가져오기() {
		return bookRepository.findAll();
	}
	
	@Transactional
	public Book 수정하기(Long id, Book book) {
		// 더티체킹 후 update
		// 영속화 -> db의 실제 값을 가져왔음 -> bookEntity(book 오브젝트)
		// 스프링 내부 메모리 공간에 데이터를 따로 들고 있음 -> 영속성API
		Book bookEntity = bookRepository.findById(id)
			.orElseThrow(()-> new IllegalArgumentException("id를 확인하세요"));
		// 영속화 -> db에서 데이터를 들고 온것 -> 영속성 컨텍스트에 보관
		// 보관된 데이터를 수정 후 리턴 
		bookEntity.setTitle(book.getTitle());
		bookEntity.setAuthor(book.getAuthor());
		return bookEntity;
// 종료 시 -> 트렌젝션 종료 -> 영속화 되어있는 데이터를 DB로 갱신 -> flush -> 더티체킹
	}
	
	@Transactional
	public String 삭제하기(Long id) {
		bookRepository.deleteById(id); // 리턴이 없음 -> 오류가 터지면 익셉션을 탐 -> 신경 x
		return "ok";
	}
	
}
