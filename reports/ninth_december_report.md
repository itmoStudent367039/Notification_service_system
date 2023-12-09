# Что сделано с прошлого раза:

### Игорь:

+ Backend:
    + Разделил на два сервиса - auth api, user api
    + Теперь аунтефикация проходит через сервис auth api - все запросы к user api попадают в AuthenticationFilter - он
      отправляет Http request (jwt token в `Authorization` header) на сервер auth - и там выдаются права из БД для
      пользователя
    + Сервисы - Auth api, User api общаются через http
    + Создал две БД:
        + Для Auth api
        + Для User api
    + Настроил cors:
        + Теперь можно отправлять запросы к backend только с домена notification-system.com(frontend), localhost(другие
          сервисы)

+ Nginx:
    + Ввел локальный домен: notification-system.com (127.0.0.1) - localhost (пока что)
    + Проксировал запросы:
        + От браузера к notification-system.com -> http://frontend:3000/
        + От браузера к auth-api -> http://auth-api:3001/
        + От браузера к user-api -> http://user-api:3002/
        + Конфигурация nginx находится в nginx.conf.prod, docker-compose.yml

+ Docker:
    + Dockerfile для каждого сервиса
        + Frontend
        + Auth-api
        + User-api
    + docker-compose.yml:
        + Used images:
            + postgres
            + nginx

### Нэля:

1. Избавилась от jQuery
2. Допрошла первую часть курса по реакту
3. Переписала фронт на реакт, теперь библиотеки подключены не через script в html , а через npm
4. Добавила отправку запросов и обработку ответов, при успешной отправке открывается страница с просьбой подтвердить
   почту. Но когда переписала на реакт, появились проблемы с подключением CSS к этой странице, пока в процессе решения
5. И теперь при вводе не валидных данных выводятся сообщения об ошибках в виде компонента реакт

### Леонид:

+ Дебажит telegram bot до воскресенья
+ До 14 февраля пишет bot для vk