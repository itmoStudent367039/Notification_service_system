# Telegram

You can send a message via a "POST" or "GET" request to the Telegram API

In general, it looks like this:
```
https://api.telegram.org/bot<BOT_TOKEN>/sendMessage?chat_id=<ID_chat>&text=<Message>
```
- We have a token initially
- Chat ID is the same chat ID with the user
- Message


**More about the chat ID**

It's not a bad idea to add to the database all the usernames that have subscribed to the bot at all, and when sending, get the user ID from this list, comparing it with the transmitted one. 

People who wrote a similar application on python say that it would be necessary to keep all users who have ever subscribed to the bot in a file, and preferably in a database

In general, dudes say that it would be necessary to write this on a python, but we are fans of counterculture.

# Mail

In my delirium I decided to use JavaMailSender for Spring Boot

What you need to send:
- Sender's address 
- Recipient's address
- Subject
- The text of the letter- 

Code example:
```java
final SimpleMailMessage simpleMail = new SimpleMailMessage();
simpleMail.setFrom("artem.boiar@yandex.ru");
simpleMail.setTo("yegor.bugaenko@huawei.com");
simpleMail.setSubject("Java 20 new hot features");
simpleMail.setText("Java 20 new hot features. No attachments :(");
```
Why I decided to change my shoes to JMS:
1. This is a library for spring and it's incredibly easy to connect
2. More code examples
3. There is not so much need to customize the code for JMS as for the same Google SMTP
4. You can send MIME[^1] messages

[^1]: it's just 10/10, with its help you can throw both files (like) and pictures (exactly) and control the nesting of content

# VK
We use the VK API obviously.

What you need to send:
1. Access Token[^2]
2. Parameters
    - ID of the user (to whom we are sending)
    - Message
    - random_id[^3]

[^2]: Copied from the VK developer page. Done once
[^3]: Needed to prevent the same message from being sent again, it is set randomly, the main thing is to set from 1-32, if you set 0, it means that the check is not needed

Code example:
```java
public void sendMessage(String msg, int peerId){
        if (msg == null){
            System.out.println("null");
            return;
        }
        try {
            vkCore.getVk().messages().send(vkCore.getActor()).peerId(peerId).message(msg).execute();
        } catch (ApiException | ClientException e) {
            e.printStackTrace();
        }
    }
```

# Conclusion

What should be in the csv file:

for the telegram:
- Chat ID is the same chat ID with the user
- Message

for mail:
- Sender's address
- Recipient's address
- Subject (as I understand it is the subject of the letter)
-The text of the letter

for vk:
- ID of the user (to whom we are sending)
- Message
