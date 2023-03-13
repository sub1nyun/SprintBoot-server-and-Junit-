import React, { useEffect, useState } from 'react';
import { Button } from 'react-bootstrap';
import { useNavigate, useParams } from 'react-router-dom';

const Detail = (props) => {

    //글 번호 받기
    const propsParam = useParams();
    const id = propsParam.id;

    const [book, setBook] = useState({
        id:"",
        title:"",
        author:""
    });

    useEffect(() => {
        fetch("http://localhost:8080/book/"+id).then((res=> res.json())).then(res=> {
        setBook(res); //새로운 객체기때문에 덮어씌움
        });
    }, []);

    let navigate = useNavigate();
    const handleGoHome = () => {
        navigate("/");
    }

    const handleGoUpdate = () => {
        navigate("/updateForm/"+id);
    }

    const deleteBook = () => {
        fetch("http://localhost:8080/book/"+id, {
            method:"DELETE"
        }).then((res=> res.text())).then(res=>{
            if(res === "ok") {
                handleGoHome();
            }else {
                alert("삭제실패함");
            }
        });
    }

    const updateBook = () => {
        handleGoUpdate()
    }

    return (
        <div>
            <h1>상세보기</h1>
            <Button variant="warning" onClick={updateBook}>수정</Button>
            {' '}
            <Button variant="danger" onClick={deleteBook}>삭제</Button>
            <hr/>
            <h3>{book.author}</h3>
            <h1>{book.title}</h1>
        </div>
    );
};

export default Detail;