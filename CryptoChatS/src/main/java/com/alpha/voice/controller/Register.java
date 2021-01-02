package com.alpha.voice.controller;

import com.alpha.voice.interfaces.UserDAO;
import com.alpha.voice.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping(path = "/avoice")
public class Register {

    @Autowired
    private UserDAO userDAO;

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    @ResponseBody
    public String addNewUser(@RequestBody User user) {
        // TODO criptat parola inainte sa o introduc in baza de date.
        //  user.setPassword("parolaCriptata");
        try {
            return this.userDAO.addUser(user);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "Fail";
    }

}
