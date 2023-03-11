import React from 'react';
import { Button, Container, Form, Nav, Navbar, NavDropdown } from 'react-bootstrap';
import { Link } from 'react-router-dom';

const Header = () => {
    return (
        <Navbar bg="dark" expand="lg" variant='dark'>
        <Container fluid>
          <Link to="/" className='navbar-brand'>홈</Link>
          <Navbar.Toggle aria-controls="navbarScroll" />
          <Navbar.Collapse id="navbarScroll">
            <Nav
              className="me-auto my-2 my-lg-0"
              style={{ maxHeight: '100px' }}
              navbarScroll
            >
              <Link to="/joinForm" className='nav-link'>회원가입</Link>  
              <Link to="/loginForm" className='nav-link'>로그인</Link>
              <Link to="/saveForm" className='nav-link'>글쓰기</Link>  
            </Nav>
            <Form className="d-flex">
              <Form.Control
                type="search"
                placeholder="Search"
                className="me-2"
                aria-label="Search"
              />
              <Button variant="outline-success">Search</Button>
            </Form>
          </Navbar.Collapse>
        </Container>
      </Navbar>
    );
};

export default Header;