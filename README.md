# SprintBoot-server-and-Junit-

## React-SpringBoot Book 프로젝트

### 스프링부트

- Sprintboot ^2.0
- JPA
- H2
- Maven
- Lombok

### React # yarn add -> npm i ~

- yarn add react-router-dom
- yarn add redux react-redux
- yarn add react-bootstrap bootstrap

```txt - index.js
import 'bootstrap/dist/css/bootstrap.min.css';
```

### 프로젝트 세팅

```json
{
  "singleQuote": true,
  "semi": true,
  "tabWidth": 2,
  "trailingComma": "all",
  "printWidth": 80
}
```

### 서버

- 스프링 8080
- 리엑트 3000
- 실제로 배포할때는 리엑트를 빌드하여 빌드폴더가 생김 -> nginx(Apache와 유사) -> 웹서버
- react 빌드 후 -> nginx로 빌드

- npm run build

1. build 라는 폴더가 생김 -> js를와 사용중인 node모듈, index.html

- index.html 읽을 수 있는 웹서버만 있으면 됨 -> 배포시에는 build만

2. AWS 다운 - front, back -> 하나의 폴더안에 두가지가 다들어옴
3. maven으로 테스트 후 컴파일, 패키징 -> jar파일 생김
4. npm test 후 빌드 -> build 폴더를 nginx로 이동

- 빌드를 위해서는 node.js 필요
- nginx 설치 -> 웹서버 폴더가 존재 -> build 폴더 옮기기
