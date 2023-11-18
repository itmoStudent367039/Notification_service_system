# Register user
-Post: {host}/api/bot/userExist

Request:
```json
{
  "tg_name": "prikolist"
}
```

Response(Success):
```json
{
"result": true
}
```
or
```json
{
"result": false
}
```


# Sending message
-Post: {host}/api/bot/sendMessage

Request:
```json
{
  "arg1": "prikolist",
  "arg2": "Kolya loh obyelsya bloh"
}
```

Response:
```json
{
"resp": "success"
}
```

# Interaction with a chatbot

1. During registration, there are two fields in the Telegram (in this order):
    - Link to the chatbot, if you do not press the link, registration will not be completed
    - Name in the Telegram 

    By clicking the "Register" button, a user Exist request is sent to the bot's server, which checks
    has the user allowed him to send messages on behalf of the bot, if the value "false" is returned, thena warning
    pops up that either the user entered an invalid name In the Telegram or he did not subscribe to the bot.
 
    P.S. I was a little confused that this warning does not say in which specific field the error is, but then I saw the registration in the github where it says that you have either the wrong password or login and realized that I was on the right track.

2. How does a bot subscription work:

    At the moment when the user clicked on the link to the bot, he opens a dialog box with the bot where the only
    how you can interact with it for the first time is the "/start" command in the form of a button.
    This command is read and checked whether there is a user who sent this command to the database, if so, the command is ignored like any other input, if not, a new user is written to the database by filling in the user name and his "chat_id" from the Update object that comes to us at the moment when the user sends a message
 
3. Sending messages

    At the request of SendMessage, a request is sent with a message and the user name in the Telegram. 
    By this name, chat_id is obtained from the local bot database and a message is sent by it
