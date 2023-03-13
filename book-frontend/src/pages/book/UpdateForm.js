import React, { useEffect, useState} from 'react';
import { Button, Form } from 'react-bootstrap';
import { json, Link, useNavigate, useParams } from 'react-router-dom';

const UpdateForm = (props) => {

    //글 번호 받기
    const propsParam = useParams();
    const id = propsParam.id;

    //Submit시에 data를 날려야 하기때문에 상태로 구분하는 것이 편리 -> onChange
    const [book, setBook] = useState({ // 값 미리 세팅해두기
        title:"",
        author:"",
    });

    useEffect(() => {
        fetch("http://localhost:8080/book/"+id).then((res=> res.json())).then(res=> {
        setBook(res); //새로운 객체기때문에 덮어씌움
        });
    }, []);

    const changeValue = (e) => {
        // 값이 변할때마다 e -> 이벤트 컨텍스트로 값이 넘어옴
        setBook({
            ...book, //기존의 book 값에 -> ...book (기존 값 보존을 위해서)
            [e.target.name]:e.target.value // 변경되는 value값이 name(title, author)값에 들어감
        });
    } 

    let navigate = useNavigate();
    const handleGoHome = () => {
        navigate("/book/"+id);
    }

    const submitBook = (e) => {
        e.preventDefault(); //submit이 action을 타지 않고 할일을 멈춤

        fetch("http://localhost:8080/book/"+id,{
            method:"PUT", 
            headers:{"Content-Type": "application/json; charset=utf-8"},
            body:JSON.stringify(book)
        },
        ).then((res=>{
            console.log(1, res);
            if(res.status === 200){
                return  res.json();
            }else {
                return null;
            }}))
        .then((res=>{ // Catch 오류 부분
            if(res !== null) {
                handleGoHome();
            }else {
                alert("수정 실패했음");
            }
        }// then 에서 실패시 작동 
        )).catch((error) => {
            console.log(error)
        });
    };

    return (
        <Form onSubmit={submitBook}>
            <Form.Group className="mb-3" controlId="formBasicEmail">
                <Form.Label>Title</Form.Label>
                <Form.Control type="text" placeholder="Enter Title" onChange={changeValue} name="title" value={book.title}/>
            </Form.Group>
            <Form.Group className="mb-3" controlId="formBasicEmail">
                <Form.Label>Author</Form.Label>
                <Form.Control type="text" placeholder="Enter Author" onChange={changeValue} name="author" value={book.author}/>
            </Form.Group>
    
            <Button variant="primary" type="submit">
            Submit
            </Button>
      </Form>
    );
};

export default UpdateForm;