<h1 align="center">Project - IN HUb</h3>
<p align="center">

<h3 align="center">IN HUB</h3>

<div align="center"><b>면접 질문 준비 IN HUB 로 끝!</b></div>

<div align="center"><a href="https://inhub.1ll.ca/"><b>🔗 show application</b></a></div>

<div align="center"><a href="https://www.youtube.com/watch?v=rHitqEZ9Zfc&feature=youtu.be"><b>🔗 Video</b></a></div>

<div align="center"><a href="https://www.notion.so/20-In-Hub-project-5855422daa1b4b2e8c0b8fb4f0c73e51?pvs=4"><b>🔗  History</b></a></div>

## about IN HUB
Project IN HUB 는 단순히 취업용 포트폴리오를 위한 프로젝트가 아닌,
실제로 우리가 일상생활에서 사용할 수 있는 것을 만들어 보자

라는 생각에서시작되었습니다.

그 중에서도

IN HUB 는취업 준비중인 개발자 지망생에게
면접에서 자주 물어보는 질문들을

효율적으로 학습할 수 있는 교육 서비스를 핵심 서비스로 제공합니다.

<br>

### Tool

<img width="848" alt="스크린샷 2023-06-16 오전 10 48 49" src="https://github.com/Team-twenty-inhub/Project-InHub/assets/115536240/d7d09870-958d-4cb9-aeef-570dc67b8b83">

### Architecture

<img width="795" alt="스크린샷 2023-06-16 오전 10 49 01" src="https://github.com/Team-twenty-inhub/Project-InHub/assets/115536240/cbbd6fa1-ddfc-465e-8efa-8d8637b98d73">

### ERD

<img width="785" alt="스크린샷 2023-06-16 오전 10 49 14" src="https://github.com/Team-twenty-inhub/Project-InHub/assets/115536240/cd839315-576d-4c08-962f-49bf558f6663">

## 핵심기능

![10](https://github.com/Team-twenty-inhub/Project-InHub/assets/115536240/64992e21-6e79-4de9-b410-5ea876305329)
![11](https://github.com/Team-twenty-inhub/Project-InHub/assets/115536240/82a5a114-b3a2-4297-b643-b593f1b8387b)
![12](https://github.com/Team-twenty-inhub/Project-InHub/assets/115536240/4d2422b0-5dc4-490f-bde0-b37854f5ad6f)
![13](https://github.com/Team-twenty-inhub/Project-InHub/assets/115536240/dbb0fc4a-4d5c-45cc-a99b-3a00c742a4f2)
![14](https://github.com/Team-twenty-inhub/Project-InHub/assets/115536240/36ecb629-902d-454f-8f51-7164ce8cc20e)
![15](https://github.com/Team-twenty-inhub/Project-InHub/assets/115536240/427f6a67-0316-4488-9f96-2d79990025ba)
![16](https://github.com/Team-twenty-inhub/Project-InHub/assets/115536240/b4382ee8-bca3-4a6f-ac78-a3431bcc69a6)
![17](https://github.com/Team-twenty-inhub/Project-InHub/assets/115536240/8b6b9d75-7c0f-4d8e-b584-598373ed4c0f)
![18](https://github.com/Team-twenty-inhub/Project-InHub/assets/115536240/ebc20b0a-9479-41dc-8578-0c25cf54b46d)
![19](https://github.com/Team-twenty-inhub/Project-InHub/assets/115536240/d96e7ced-9375-4cf9-a4e3-6ff9ac4371e7)
![20](https://github.com/Team-twenty-inhub/Project-InHub/assets/115536240/bf013a06-8bb1-4e80-bde4-43461f300eac)

## Ground rule

```java

@GetMapping("/")  // 애노테이션 마다 한 줄
public class Controller {  // 카멜표기법
// 들여쓰기 공백 4개(기본 tab 1번)
    private Service service;

    public void method (  // 파라미터가 너무 길 경우, 한 줄 내리기
        @RequestParam String value,
        HttpServletResponse resp,
		    HttpServletRequest req
    ) {
        
    }
}
```

- 기본적으로 인텔리제이에 내장된 포맷터 단축키 사용 ( Ctrl + Alt + L )

[[JAVA] 코딩 컨벤션에 대해서](https://velog.io/@ozragwort/JAVA-코딩-컨벤션에-대해서)

### commit message

```yaml
feat: 새로운 기능 추가
design: HTML 변경
test: 테스트 코드 관련

fix: 버그 수정
docs: 문서 수정

refactor: 코드 리팩터링, 성능 개선
remove: 파일을 삭제

chore: 그 외 자잘한 수정
```

### directory 구조

- base
    - security
    - init db
    - event
    - base entity
- ut
- bounded context
    - domain
        - entity
        - repository
        - controller
            - form
        - service

- template
    - admin
    - usr
        - member
            - fragment
                - create (create 관련 fragment 모듈)
                    - form.html
            - top
                - create.html


### service method name

- create
    - create
- read
    - find
- update
    - update
- delete
    - delete