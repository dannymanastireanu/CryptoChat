package com.alpha.voice.controller;

import com.alpha.voice.interfaces.UserDAO;
import com.alpha.voice.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.function.EntityResponse;

import javax.crypto.Cipher;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.util.Base64;

@Controller
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping(path = "/avoice/crypto")
public class Crypto {
    @Autowired
    private UserDAO userDAO;

    @RequestMapping(value = "/pubKey", method = RequestMethod.GET)
    @ResponseBody
    public String getPublicServerKey(@CookieValue("sessionUserId") String sessionUserId) {
        try {
            String pbKey = this.userDAO.generatePairKey(sessionUserId);
            if(!pbKey.isEmpty())
                return pbKey;
//            new ResponseEntity<>("Year of birth cannot be in the future", HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "404";
    }

    @RequestMapping(value = "/pubKey", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<?> setPublicClientKey(@CookieValue("sessionUserId") String sessionUserId, @RequestBody String pbKey) {
        try {
            Integer result = this.userDAO.updatePublicClientKey(sessionUserId, pbKey);
            if(result != -1)
                return new ResponseEntity<>(HttpStatus.CREATED);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @RequestMapping(value = "/aes", method = RequestMethod.POST)
    @ResponseBody
    public String setAESClientKey(@RequestBody String message) {
        try {
            System.out.println(message);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return "404";
    }
}
