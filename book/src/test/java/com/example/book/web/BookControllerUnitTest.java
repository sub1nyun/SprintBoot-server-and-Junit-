package com.example.book.web;


import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.web.bind.annotation.PostMapping;

import com.example.book.domain.Book;
import com.example.book.service.BookService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;

import ch.qos.logback.core.status.Status;
import lombok.extern.slf4j.Slf4j;


//단위 테스트 -> 컨트롤러만 테스트 -> Controller 관련 로직만 IoC 띄움 
//Filter, ControllerAdvice -> 익셉션 처리할 때 섰음
//컨트롤러를 위한 객체들이 메모리에 뜸 -> 전체가 뜨지않아 가벼움

@Slf4j // log
@WebMvcTest // 실제 Controller, Filter, advice를 IoC에 띄우는 기능
//@ExtendWith(SpringExtension.class) -> Jnuit5 테스트지만 위 어노테이션에 들어가있음
//@RunWith(SpringRunner.class) // 필수 -> Junit4 테스트를 할때 스프링 환경에서 하고 싶다면
public class BookControllerUnitTest {

	@Autowired //@WebMvcTest안에 @AutoConfigureMockMvc 있어서 DI 가능
	private MockMvc mockMvc;
	
	@MockBean //(가짜) // IoC 환경에 bean 등록됨
	private BookService bookService;
	
	//BDDMockito 패턴 given, when, then
	@Test
	public void save_테스트() throws Exception {
		// given 테스트를 하기 위한 준비
		// object를 json으로 바꾸는 함수 -> 저장하기() json 타입 데이터가 필요해서
		Book book = new Book(null, "스프링 따라하기", "코스");
		String content = new ObjectMapper().writeValueAsString(book);
		// {"id":null,"title":"스프링 따라하기","author":"코스"}
		// 가정법 -> 미리 행동을 지정하는 것 -> 리턴 결과 값 지정해두기      ▼
		when(bookService.저장하기(book)).thenReturn(new Book(1L, "스프링 따라하기", "코스"));
		
		// when -> 테스트 실행
		// perform 리턴 타입 -> ResultActions
		ResultActions resultActions =  mockMvc.perform(MockMvcRequestBuilders.post("/book") 
				// 던지는 데이터 타입
				.contentType(MediaType.APPLICATION_JSON)
				// 실제로 던질 데이터
				.content(content)
				.accept(MediaType.APPLICATION_JSON));
		
		// then (검증)
		resultActions.andExpect(MockMvcResultMatchers.status().isCreated())
		.andExpect(MockMvcResultMatchers.jsonPath("$.title").value("스프링 따라하기"))
		// $ 전체 결과 .변수가 value("~") 가 맞는지 검증
		.andDo(MockMvcResultHandlers.print()); // 콘솔에 결과를 보여줌
	}
	
	@Test
	public void find_All_테스트() throws Exception {
		// given 
		// 현재는 미리 필요한 데이터가 없지만 롤백됐으니 생성해줌
		List<Book> books = new ArrayList<>();
		books.add(new Book(1L, "스프링부트 따라하기", "코스"));
		books.add(new Book(2L, "리엑트 따라하기", "코스"));
		when(bookService.모두가져오기()).thenReturn(books);
		
		//when 
		//MockMvcRequestBuilders import 절에 static 으로 선언하면 줄여서 사용이 가능함
		//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
		ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.get("/book")
				.accept(MediaType.APPLICATION_JSON));
		
		//then 을 안 적으면 테스트 100% 보장하기 어려움
		// isOk() 리턴 값 -> ResultMatcher
		resultActions
			.andExpect(MockMvcResultMatchers.status().isOk())
			.andExpect(MockMvcResultMatchers.jsonPath("$", Matchers.hasSize(2)))
			.andExpect(MockMvcResultMatchers.jsonPath("$.[0].title").value("스프링부트 따라하기"))
			.andDo(MockMvcResultHandlers.print());
			
			
	}



	
}
