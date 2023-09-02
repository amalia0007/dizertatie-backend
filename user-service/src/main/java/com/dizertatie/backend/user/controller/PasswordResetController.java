package com.dizertatie.backend.user.controller;

import com.dizertatie.backend.user.exception.VerificationTokenException;
import com.dizertatie.backend.user.model.PasswordForgottenDTO;
import com.dizertatie.backend.user.repository.UserRepository;
import com.dizertatie.backend.user.service.EmailServiceImpl;
import com.dizertatie.backend.user.model.PasswordResetDTO;
import com.dizertatie.backend.user.service.PasswordResetServiceImpl;
import com.dizertatie.backend.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

@RestController
public class PasswordResetController {

    private String key;

    Map<String, String> map = new HashMap<>();

    @Autowired
    private UserService userService;

    @Autowired
    private EmailServiceImpl emailServiceImpl;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordResetServiceImpl passwordResetServiceImpl;

    @PostMapping("/resetPassword")
    public void resetPassword(@RequestBody PasswordResetDTO passwordResetDTO, @RequestParam("random") String random) {

        String k = map.get(passwordResetDTO.getEmail());
        if (random.equals(k)) {
            userService.saveNewPassword(passwordResetDTO.getEmail(), passwordResetDTO.getPassword());
        } else {
            throw new VerificationTokenException("Invalid token");
        }
    }


    @PostMapping("/forgotPassword")
    public void forgotPassword(@RequestBody PasswordForgottenDTO passwordForgottenDTO, HttpServletRequest request) {

        if (userRepository.findByEmail(passwordForgottenDTO.getEmail()) != null) {

            String random = passwordResetServiceImpl.getAlphaNumericString(10);


            URL url = null;
            try {
                url = new URL(request.getRequestURL().toString().replace("/forgotPassword", "") + "/resetPassword?random=" + random);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }

            emailServiceImpl.sendResetPasswordEmail(userService.findUserByEmail(passwordForgottenDTO.getEmail()), random);

            key = random;

            map.put(passwordForgottenDTO.getEmail(), key);


        } else {
            System.out.println("This email doesn't exist in the database!");
        }
    }

}
