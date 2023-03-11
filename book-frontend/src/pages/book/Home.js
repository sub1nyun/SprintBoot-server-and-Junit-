import React, { useEffect, useState } from 'react';
import BookItem from '../../components/BookItem';

const Home = () => {

    // 스프링 부트 데이터 다운받기 -> 처음엔 몇건인지 몰라서 빈 배열
    const [books, setBooks] = useState([]);

    //함수 실행시 최초 한번 실행 + 상태값이 변경될때마다 실행
    useEffect(() => { //CORS 정책(기본정책) -> 외부 js 요청을 막는 것
        fetch("http://localhost:8080/book", {method:"GET"}) // 비동기 함수 
            .then((res)=> res.json())
                .then((res)=>{console.log(1, res);
                    setBooks(res);
                }); 
                // 배열의(데이터) 내용이 바뀌면 키를 기준으로 다시 그려짐
                // 구분값인 key를 넘겨주기
        // fetch -> 요청, 주소, 오브젝트(meethod 기본 값 GET 생략 가능함)
        //처음 then -> 약속을 받음(티켓) 비어있는 통장만 주는 -> 데이터가 들어오면 주겠다
    },[]) //[] -> 의존하지 않는다는 의미 명시

    return (
        <div>
            {books.map((book) => <BookItem key={book.id} book={book}/>)}         
        </div>
    );
};

export default Home;