package com.alpha.voice.controller;

import com.alpha.voice.interfaces.MessageDAO;
import com.alpha.voice.interfaces.UserDAO;
import com.alpha.voice.model.Friend;
import com.alpha.voice.model.Message;
import com.alpha.voice.utils.Encryption;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping(path = "/avoice")
@Controller
public class Messenger {

    @Autowired
    private SimpMessagingTemplate smtp;
    @Autowired
    private UserDAO userDAO;
    @Autowired
    private MessageDAO messageDAO;


    @MessageMapping("/chat/{to}")
     public void sendMessage(@DestinationVariable String to, Message message) {
        if(message.getType().equals("text")) {
            String pvtKey = this.userDAO.getPrivateKeyServer(message.getFrom());
            if(pvtKey != null) {
                String decryptMessage = Encryption.decryptRSA(message.getBody(), pvtKey);
                message.setBody(decryptMessage);
                Integer result = this.messageDAO.store(message);
                if(result != -1){
                    System.out.println("Successfully stored messages");
                }
                String pbKey = this.userDAO.getPublicKeyClient(message.getTo());
                String encryptedMessage = Encryption.encryptRSA(message.getBody(), pbKey);
                message.setBody(encryptedMessage);
                smtp.convertAndSend("/topic/messages/" + to, message);
            } else {
                System.out.println("Null private key");
            }
        } else if(message.getType().equals("image")) {
            Integer result = this.messageDAO.store(message);
            if(result != -1){
                System.out.println("Successfully stored messages");
            }
            smtp.convertAndSend("/topic/messages/" + to, message);
        } else{
            System.out.println("IDK format");
        }
     }


    @RequestMapping(value = "/friends", method = RequestMethod.GET)
    @ResponseBody
    public String getFriends(@CookieValue("sessionUserId") String sessionUserId) {
        List<Friend> friends = this.userDAO.getFriends(sessionUserId);
        if(friends != null) {
            return friends.toString();
        } else{
            return "404";
        }
    }

    @RequestMapping(value = "/messages/{user2Username}", method = RequestMethod.GET)
    @ResponseBody
    public String getMessagesFriend(@CookieValue("sessionUserId") String sessionUserId, @PathVariable String user2Username) {
        List<Message> messages = this.messageDAO.getFriendMessages(sessionUserId, user2Username);
        if(messages != null){
            return messages.toString();
        } else {
            return "404";
        }
    }

    // TODO Validate server websocket connection

    // TODO Notify user when is new message if the user is still connected
}
