# Что сделано с прошлого раза:

### Игорь:

+ Backend
    + (https://github.com/IfmoCatsProjects/Notification_service_system/commit/80361b5cd011faa2f0f9c9f66ec3d4f4516c2218):
    + Разделил на два сервиса - auth api, user app
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

+ Nginx
    + (https://github.com/IfmoCatsProjects/Notification_service_system/commit/1ce9ca91a604792e8a6b55c76cc8e8837292abe8#diff-3e742d0d1c64b9eff1bd1157d958580ebe886f72431941f2fcf8d6003a031edc):
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
    + docker-compose.yml - marked ignore, because it contains different person information:
        + Used images:
            + postgres
            + nginx

### Нэля:

1. Избавилась от jQuery
2. Допрошла первую часть курса по реакту
3. Переписала фронт на реакт, теперь библиотеки подключены не через script в html , а через
   npm (https://github.com/IfmoCatsProjects/Notification_service_system/commit/66b3b9bcf4851677f4638f8d0de71d26207a2092)
4. Добавила отправку запросов и обработку ответов, при успешной отправке открывается страница с просьбой подтвердить
   почту. Но когда переписала на реакт, появились проблемы с подключением CSS к этой странице, пока в процессе
   решения (https://github.com/IfmoCatsProjects/Notification_service_system/commit/d94a37241791e47fbd8e172befc53f522ca191c9)
5. И теперь при вводе не валидных данных выводятся сообщения об ошибках в виде компонента
   реакт (https://github.com/IfmoCatsProjects/Notification_service_system/commit/ad90a701984f6c39f8a26c7afa82cfe4a8330102), (https://github.com/IfmoCatsProjects/Notification_service_system/commit/03e0238cf046861c16adec482eba8ab1593a9632)

### Леонид:

+ Дебажит telegram bot до воскресенья
+ До 14 февраля пишет bot для vk