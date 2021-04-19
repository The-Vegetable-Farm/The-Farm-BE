# The Farm

&nbsp;

&nbsp;

&nbsp;

작성자 : 김보현

작성일자 : 2021.04.19

&nbsp;

&nbsp;

&nbsp;

### 1. 개요

본 문서는 사용자 관리, 미니게임 API 관련 문서입니다.

&nbsp;

&nbsp;

&nbsp;

### 2. 환경설정

#### 2.1 개발환경

- SQL
  - mariadb
    - username : devusr
    - password : thefarm!1
- NoSQL
  - redis

&nbsp;

&nbsp;

#### 2.2 운영환경



&nbsp;

&nbsp;

&nbsp;

### 3. 사용자 관리 API

#### 3.1 아이디 중복 체크

[Request]

- host : {host}/user/check/account/{account}
- method : GET
- parameter

| name    | type   | desc   | 필수값 |
| ------- | ------ | ------ | ------ |
| account | String | 아이디 | Y      |

- example

```json
http://localhost/user/check/account/bh
```

&nbsp;

[Response]

- example

```json
false
```

&nbsp;

&nbsp;

#### 3.2 이메일 중복 체크

[Request]

- host : {host}/user/check/email/{email}
- method : GET
- parameter

| name  | type   | desc   | 필수값 |
| ----- | ------ | ------ | ------ |
| email | String | 이메일 | Y      |

- example

```json
http://localhost/user/check/email/bohyun8991@gmail.com
```

&nbsp;

[Response]

- example

```json
false
```

&nbsp;

&nbsp;

#### 3.3 닉네임 중복 체크

[Request]

- host : {host}/user/check/nickname/{nickname}
- method : GET
- parameter

| name     | type   | desc   | 필수값 |
| -------- | ------ | ------ | ------ |
| nickname | String | 닉네임 | Y      |

- example

```json
http://localhost/user/check/nickname/테스트
```

&nbsp;

[Response]

- example

```json
false
```

&nbsp;

&nbsp;

#### 3.4 이메일 인증번호 전송

이메일로 전송된 인증번호를 3분 이내에 입력해야 합니다.

[Request]

- host : {host}/user/mail/certnum/{email}
- method : GET
- parameter

| name  | type   | desc   | 필수값 |
| ----- | ------ | ------ | ------ |
| email | String | 이메일 | Y      |

- example

```json
http://localhost/user/mail/certnum/bohyun8991@gmail.com
```

&nbsp;

[Response]

- example

```json
true
```

&nbsp;

&nbsp;

#### 3.5 이메일 인증번호 확인

이메일로 전송된 인증번호를 3분 이내에 입력해야 합니다.

[Request]

- host : {host}/user/check/certnum/{email}/{certnum}
- method : GET
- parameter

| name    | type   | desc     | 필수값 |
| ------- | ------ | -------- | ------ |
| email   | String | 아이디   | Y      |
| certnum | String | 인증번호 | Y      |

- example

```json
http://localhost/user/check/certnum/bohyun8991@gmail.com/617402
```

&nbsp;

[Response]

- example

```json
true
```

&nbsp;

&nbsp;

#### 3.6 회원가입

비밀번호는 BCryptPassword, 이메일은 AES 를 통해 암호화 후 데이터베이스에 저장합니다.

[Request]

- host : {host}/user/signup
- method : POST
- parameter

| name     | type   | desc                | 필수값 |
| -------- | ------ | ------------------- | ------ |
| account  | String | 아이디              | Y      |
| password | String | 비밀번호 (8~14자리) | Y      |
| email    | String | 인증받은 이메일     | Y      |
| nickname | String | 닉네임              | Y      |

- example

```json
{
    "account":"bh",
    "password":"test1234!", 
    "email":"bohyun8991@gmail.com",
    "nickname":"보보킴"
}
```

&nbsp;

[Response]

- example

```json
{
    "result": "Y",
    "role": "ROLE_USER",
    "nickname": "보보킴",
    "account": "bh",
    "email": "bohyun8991@gmail.com"
}
```

&nbsp;

&nbsp;

#### 3.7 로그인

[Request]

- host : {host}/user/signin
- method : POST
- parameter

| name     | type   | desc                | 필수값 |
| -------- | ------ | ------------------- | ------ |
| account  | String | 아이디              | Y      |
| password | String | 비밀번호 (8~14자리) | Y      |

- example

```json
{
    "account":"bh",
    "password":"test1234!"
}
```

&nbsp;

[Response]

- example

```json
true
```

&nbsp;

&nbsp;

#### 3.8 최근 접속 시간 저장

[Request]

- host : {host}/user/last/{account}
- method : PATCH
- parameter

| name    | type   | desc   | 필수값 |
| ------- | ------ | ------ | ------ |
| account | String | 아이디 | Y      |

- example

```json
http://localhost/user/last/bh
```

&nbsp;

&nbsp;

#### 3.9 회원정보 조회

[Request]

- host : {host}/user/{account}
- method : GET
- parameter

| name    | type   | desc   | 필수값 |
| ------- | ------ | ------ | ------ |
| account | String | 아이디 | Y      |

- example

```json
http://localhost/user/bh
```

&nbsp;

[Response]

- example

```json
{
    "result": "Y",
    "regTime": "2021-04-19 10:22:05",
    "role": "ROLE_USER",
    "nickname": "보보킴",
    "characterCnt": 0,
    "id": 2,
    "account": "bh"
}
```

&nbsp;

&nbsp;

#### 3.10 계정 휴면처리

[Request]

- host : {host}/user/{account}
- method : GET
- parameter

| name    | type   | desc   | 필수값 |
| ------- | ------ | ------ | ------ |
| account | String | 아이디 | Y      |

- example

```json
http://localhost/user/bh
```

&nbsp;

[Response]

- example

```json
true
```

&nbsp;

&nbsp;

#### 3.11 회원정보 수정

[Request]

- host : {host}/user
- method : PATCH
- parameter

| name     | type   | desc     | 필수값 |
| -------- | ------ | -------- | ------ |
| account  | String | 아이디   | Y      |
| password | String | 비밀번호 | N      |
| nickname | String | 닉네임   | N      |
| email    | String | 이메일   | N      |

- example

```json
{
    "account":"bh",
    "nickname":"가나다"
}
```

&nbsp;

[Response]

- example

```json
{
    "result": "Y",
    "nickname": "가나다",
    "account": "bh"
}
```

&nbsp;

&nbsp;

#### 3.12 회원탈퇴

[Request]

- host : {host}/user/{account}
- method : DELETE
- parameter

| name    | type   | desc   | 필수값 |
| ------- | ------ | ------ | ------ |
| account | String | 아이디 | Y      |

- example

```json
http://localhost/user/bh
```

&nbsp;

[Response]

- example

```json
true
```

&nbsp;

&nbsp;

