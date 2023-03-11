package com.example.book.web;

import static org.junit.jupiter.api.Assertions.assertEquals;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.annotation.Transactional;
import com.example.book.domain.Book;
import com.example.book.domain.BookRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;


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

/**
 * 통합 테스트 하는 법 단점 : 모든 빈이 다 로드되기 때문에 통합 테스트가 가능하지만 느림!! 장점 : 실제 서비스와 가장 유사하게 테스트
 * 가능. 팁 : SpringBootTest(class= {BookController.class, BookService.class,
 * BookRepository.class}) 이렇게 필요한 빈만 올릴 수 도 있음.
 */



//@Slf4j
@Transactional // 모든 트랜잭션 작업이 각각의 테스트 종료후 rollback 된다.
@AutoConfigureMockMvc  // MockMvc를 빈으로 등록해준다.
@SpringBootTest(webEnvironment = WebEnvironment.MOCK)  // 서블릿을 mocking 한것이 동작한다. (내장 톰켓 사용 안함)
public class BookControllerIntegreTest {
	//Integration -> 통합테스트
	//나머지 사항 복습하기
	
	
	@Autowired 
	private MockMvc mockMvc;
	
	@Autowired // 실환경이라 DI 가능
	private BookRepository bookRepository;
	
	@Autowired //JPA가 구현체임
	private EntityManager entityManager;
	
	@BeforeEach // 모든 테스트메서드가 실행 되기직전에 각각 실행 됨
	public void init() { //                                       ▼ h2 쿼리문
		entityManager.createNativeQuery("ALTER TABLE book ALTER COLUMN id RESTART WITH 1").executeUpdate();
	}
	
	
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
	
	@Test
	public void find_All_테스트() throws Exception {
		// given 
		List<Book> books = new ArrayList<>();
		books.add(new Book(1L, "스프링부트 따라하기", "코스"));
		books.add(new Book(2L, "리엑트 따라하기", "코스"));
		books.add(new Book(3L, "JUnit 따라하기", "코스"));
		bookRepository.saveAll(books); // 실제 db에 데이터 추가

		//when 
		ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.get("/book")
				.accept(MediaType.APPLICATION_JSON));
		
		//then
		resultActions
			.andExpect(MockMvcResultMatchers.status().isOk())
			.andExpect(MockMvcResultMatchers.jsonPath("$", Matchers.hasSize(3)))
			.andExpect(MockMvcResultMatchers.jsonPath("$.[2].title").value("JUnit 따라하기"))
			.andDo(MockMvcResultHandlers.print());
	}
	
	@Test
	public void findById_테스트() throws Exception {
		//given 
		Long id = 2L;
		
		// 데이터가 들어가 있어야 조회가 가능함
		List<Book> books = new ArrayList<>();
		books.add(new Book(null, "스프링부트 따라하기", "코스"));
		books.add(new Book(null, "리엑트 따라하기", "코스"));
		books.add(new Book(null, "JUnit 따라하기", "코스"));
		bookRepository.saveAll(books);
		
		//when																																					▼id 넣어줌
		ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.get("/book/{id}", id)
				//find -> 줘야할 타입 없음, 던져줄 데이터도 없음
				// 기대하기만 
				.accept(MediaType.APPLICATION_JSON));
		
		//then 검증
		resultActions
			.andExpect(MockMvcResultMatchers.status().isOk())
			.andExpect(MockMvcResultMatchers.jsonPath("$.title").value("리엑트 따라하기"))
			.andDo(MockMvcResultHandlers.print());
	}
	
	@Test
	public void update_테스트() throws Exception {
		//given 
		Long id = 3L; // update할 아이디
		// update 하기 위한 기본 데이터 세팅
		// @AfterEach 에서 bookRepository.deleteAll(); 
		List<Book> books = new ArrayList<>();
		books.add(new Book(null, "스프링부트 따라하기", "코스"));
		books.add(new Book(null, "리엑트 따라하기", "코스"));
		books.add(new Book(null, "JUnit 따라하기", "코스"));
		bookRepository.saveAll(books);
		
		// 리턴 될 데이터
		Book book = new Book(null, "C++ 따라하기", "코스");
		String content = new ObjectMapper().writeValueAsString(book);
		

		//when
		ResultActions resultActions =  mockMvc.perform(MockMvcRequestBuilders.put("/book/{id}", id) 
				.contentType(MediaType.APPLICATION_JSON)
				.content(content)
				.accept(MediaType.APPLICATION_JSON));
		
		//then 검증
		resultActions
			.andExpect(MockMvcResultMatchers.status().isOk())
			.andExpect(MockMvcResultMatchers.jsonPath("$.id").value(3L))
			.andExpect(MockMvcResultMatchers.jsonPath("$.title").value("C++ 따라하기"))
			.andDo(MockMvcResultHandlers.print());
	}
	
	@Test
	public void delete_테스트() throws Exception {
		//given 
		Long id = 1L; 
		bookRepository.saveAll(Arrays.asList(new Book(null, "스프링부트 따라하기", "코스"), new Book(null, "리엑트 따라하기", "코스")));

		//when
		ResultActions resultActions =  mockMvc.perform(MockMvcRequestBuilders.delete("/book/{id}", id));
		
		//then 검증
		resultActions
			.andExpect(MockMvcResultMatchers.status().isOk())
			.andDo(MockMvcResultHandlers.print());
		
		// 문자를 응답하면 사용
		MvcResult requestResult = resultActions.andReturn();
		String result = requestResult.getResponse().getContentAsString();
		// ok가 맞는지 검증
		assertEquals("ok", result);
	}
	
	
	
	
}
