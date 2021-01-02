package com.alpha.voice.controller;

import com.alpha.voice.interfaces.UserDAO;
import com.alpha.voice.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.sql.PreparedStatement;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

@Controller
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping(path = "/avoice")
public class Login {

    @Autowired
    private UserDAO userDAO;

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<?> authentication(@RequestBody User user, HttpServletResponse response) {
        // TODO criptat parola user ului si dupa comparat cu cea din baza de date
        try {
            User conUser = this.userDAO.getRegistredUser(user);
            if(conUser != null) {
                String uuid = UUID.randomUUID().toString();
                response.setHeader("Access-Control-Allow-Origin", "http://localhost:4200");
                response.setHeader("Access-Control-Allow-Headers",
                        "Date, Content-Type, Accept, X-Requested-With, Authorization, From, X-Auth-Token, Request-Id");
                response.setHeader("Access-Control-Expose-Hetaders", "Set-Cookie");
                response.setHeader("Access-Control-Allow-Credentials", "true");
                response.setHeader("Allow-Origin-With-Credentials", "true");
                // pt ReactNative sa salvez si eu undeva uuid ul
                response.setHeader("uuid", uuid);

                Cookie cookie = new Cookie("sessionUserId", uuid);
                cookie.setMaxAge(99999);
                cookie.setSecure(false);
                response.addCookie(cookie);

                this.userDAO.addUserSession(user, uuid);

                return new ResponseEntity<>(HttpStatus.CREATED);
            } else {
                return new ResponseEntity<>(HttpStatus.FORBIDDEN);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @RequestMapping(value = "/validate", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<Integer> validateUser(@RequestBody User user) {
        Integer userid = this.userDAO.validateUser(user);
        if(userid != -1)
            return ResponseEntity.ok(userid);
        else
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

}
