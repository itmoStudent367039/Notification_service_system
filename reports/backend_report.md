# Backend

### Registration user:

+ ***Post: {host}/auth/registration***

***Request:***

```json
{
  "username": "yestai",
  "email": "igor.abdullin.95@mail.ru",
  "password": "123"
}
```

***Response (Success, status = 201):***


```json
{
  "timestamp": "2023-11-09T22:25:22.0969513+03:00",
  "message": "successfully register",
  "data": {
    "username": "yestai",
    "email": "igor.abdullin.95@mail.ru",
    "role": "ROLE_USER",
    "telegramChatId": null,
    "vkId": null
  }
}
```

***Response (Bad Request, status = 400):***

*All not valid fields will be marked:*

```json
{
  "message": {
    "email": "This email already reserved",
    "username": "This username already reserved"
  },
  "timestamp": "2023-11-09T22:27:10.3087017+03:00"
}
```

```json
{
  "message": {
    "password": "Password size is between 2 and 31 symbols",
    "email": "Email should be valid",
    "username": "Username size is between 2 and 31 symbols"
  },
  "timestamp": "2023-11-09T22:28:41.1602234+03:00"
}
```

### Confirm user account:

+ ***Send email to user with a link***
+ ***After click user confirms his account and receive a page, which has information about it - successfully or not***
+ ***If user tries to manipulate his account without confirming, he gets forbidden response (code = 403)***
----
Hello yestai,

Your new account has been created. Please click the link below to verify your account.

http://localhost:8080/auth/confirm?token=eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiJVc2VyIGRldGFpbHMiLCJlbWFpbCI6Imlnb3IuYWJkdWxsaW4uOTVAbWFpbC5ydSIsImlhdCI6MTY5OTU1NzkyMiwiaXNzIjoieWVzdGFpIiwiZXhwIjoxNjk5NTU4ODIyfQ.4Nxa7wFpEVRePar8rJJSFqm0vK6QK-_3_JFeSnC5p8Q

® Notification Service System

---

### Login user:

+ ***Post: {host}/auth/login***

***Request:***

```json
{
  "email": "igor.abdullin.95@mail.ru",
  "password": "123"
}
```

***Response (Success, status = 200):***
+ *Also, in header - "Authorization" exists a jwt token, which frontend should send to Server, after login to get or set any data(add this token to header - "Authorization" with format: "Bearer {token}"), whitout it user will receive 403 code*

```json
{
  "timestamp": "2023-11-09T22:48:45.5832515+03:00",
  "message": "successfully login",
  "data": {
    "username": "yestai",
    "email": "igor.abdullin.95@mail.ru",
    "role": "ROLE_USER",
    "telegramChatId": null,
    "vkId": null
  }
}
```

***Response (Unauthorized, status = 401):***

+ *For example: if uncorrected password or email:*

```json
{
  "message": {
    "error": "Неверные учетные данные пользователя"
  },
  "timestamp": "2023-11-09T22:50:16.1737898+03:00"
}
```
***Response (Bad Request, status = 400):***
+ *For example: if not valid password or email:*
```json
{
  "message": {
    "password": "Password size is between 2 and 31 symbols"
  },
  "timestamp": "2023-11-09T22:53:22.4299823+03:00"
}
```
### All valid constraints:
+ ***Login:***
```java
@NotEmpty(message = "Email should not be empty")
@Email(message = "Email should be valid")
@Size(max = 63, message = "Email should be less or equals 63 symbols")
private String email;

@NotEmpty(message = "Password could not be empty")
@Size(min = 2, max = 31, message = "Password size is between 2 and 31 symbols")
private String password; 
```

+ ***Registration:***
```java
@NotEmpty(message = "Username could not be empty")
@Size(min = 2, max = 31, message = "Username size is between 2 and 31 symbols")
private String username;

@NotEmpty(message = "Password could not be empty")
@Size(min = 2, max = 31, message = "Password size is between 2 and 31 symbols")
private String password;

@NotEmpty(message = "Email should not be empty")
@Email(message = "Email should be valid")
@Size(max = 63, message = "Email should be less or equals 63 symbols")
private String email;
```

```java
@NotEmpty => string != null && !string.isEmpty 
```
