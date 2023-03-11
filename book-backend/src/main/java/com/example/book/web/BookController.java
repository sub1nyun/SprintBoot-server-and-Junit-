package com.example.book.web;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.book.domain.Book;
import com.example.book.service.BookService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
public class BookController {
	
	private final BookService bookService;
	
	@PostMapping("/book")
	public ResponseEntity<?> save(@RequestBody Book book) {
		// 리턴시 알아서 타입에 맞게 리턴 됨
		return new ResponseEntity<>(bookService.저장하기(book), HttpStatus.CREATED);
	}
	
	@GetMapping("/book")
	//? 인 이유는 현재 리턴할 것이 뭔지 모르기도하고 관리하기가 편함
	//ResponseEntity -> 리턴 할때 http 상태코드를 같이 전송이 가능
	public ResponseEntity<?> findAll() {
		return new ResponseEntity<>(bookService.모두가져오기(), HttpStatus.OK);
		// ok -> 200
	}
	
	@GetMapping("/book/{id}")
	public ResponseEntity<?> findById(@PathVariable Long id) {
		return new ResponseEntity<>(bookService.한건가져오기(id), HttpStatus.OK);
	}
	
	@PutMapping("/book/{id}")
	// update 시에는 -> 데이터를 두 건 받아야 가능함
	public ResponseEntity<?> update(@PathVariable Long id, @RequestBody Book book) {
		return new ResponseEntity<>(bookService.수정하기(id, book), HttpStatus.OK);
	}
	
	@DeleteMapping("/book/{id}")
	// update 시에는 -> 데이터를 두 건 받아야 가능함
	public ResponseEntity<?> deleteById(@PathVariable Long id) {
		return new ResponseEntity<>(bookService.삭제하기(id), HttpStatus.OK);
	}
	

}
