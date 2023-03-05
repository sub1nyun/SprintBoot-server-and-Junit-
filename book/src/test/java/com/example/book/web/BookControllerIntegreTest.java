package com.example.book.web;

import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.annotation.Transactional;

import com.example.book.domain.Book;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

/**
 * 통합 테스트 -> 모든 Bean들을 똑같이 IoC에 올리고 테스트 하는 것
 * webEnvironment = WebEnvironment.MOCK 
 * 실제 톰켓을 올리는 게 아니라, 다른 톰켓으로 테스트
 * WebEnvironment.RANDOM_POR => 실제 톰켓으로 테스트
 * @AutoConfigureMockMvc -> MockMvc를 IoC에 등록
 * @Transactional -> 각각의 테스트 함수가 종료될 때마다 트랜잭션을 rollback 하기
 * @author User
 *
 */

@Slf4j
@Transactional
@AutoConfigureMockMvc 
@SpringBootTest(webEnvironment = WebEnvironment.MOCK)
public class BookControllerIntegreTest {

	@Autowired 
	private MockMvc mockMvc;
	
	//BDDMockito 패턴 given, when, then
	@Test
	public void save_테스트() throws Exception {
		// given 테스트를 하기 위한 준비
		// object를 json으로 바꾸는 함수 -> 저장하기() json 타입 데이터가 필요해서
		Book book = new Book(null, "스프링 따라하기", "코스");
		String content = new ObjectMapper().writeValueAsString(book);
		// {"id":null,"title":"스프링 따라하기","author":"코스"}
		// 통합 환경이기에 실제 서비스가 실행 됨 -> db 저장 후 -> rollback 됨
	
		
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
	
	
	
	
}