package com.dizertatie.backend.user.controller;

import com.dizertatie.backend.user.exception.EmailExistsException;
import com.dizertatie.backend.user.exception.VerificationTokenException;
import com.dizertatie.backend.user.model.RegistrationDTO;
import com.dizertatie.backend.user.model.User;
import com.dizertatie.backend.user.model.VerificationToken;
import com.dizertatie.backend.user.repository.VerificationTokenRepository;
import com.dizertatie.backend.user.service.EmailServiceImpl;
import com.dizertatie.backend.user.service.UserService;
import com.dizertatie.backend.user.service.UserServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.LocalDateTime;

@RestController
public class ActivateAccountController {

    private final UserService userService;
    private final VerificationTokenRepository verificationTokenRepository;
    private final EmailServiceImpl emailServiceImpl;

    private final Logger log = LoggerFactory.getLogger(getClass().getName());

    @Autowired
    public ActivateAccountController(UserServiceImpl userService, VerificationTokenRepository verificationTokenRepository, EmailServiceImpl emailServiceImpl) {
        this.userService = userService;
        this.verificationTokenRepository = verificationTokenRepository;
        this.emailServiceImpl = emailServiceImpl;
    }

    @PostMapping("/register")
    @Transactional
    public User registerUserAccount(@RequestBody RegistrationDTO userDto, HttpServletRequest request) throws EmailExistsException {

        User registered = userService.registerNewUserAccount(userDto);
        VerificationToken token = new VerificationToken(registered);
        VerificationToken savedToken = verificationTokenRepository.save(token);
        String code = savedToken.getToken();

        URL url;
        try {
            url = new URL(request.getRequestURL().toString().replace("/register", "") + "/registerConfirm?token=" + savedToken.getToken());
        } catch (MalformedURLException e) {
            log.error("The user is currently disabled!");
            e.printStackTrace();
        }

        emailServiceImpl.sendActivateAccountEmail(userService.findUserByEmail(userDto.getEmail()), code);
        log.info("Activation email sent to new user.");

        return registered;

    }

    @PostMapping("/resendVerificationLink")
    public void resendVerification(HttpServletRequest request, @RequestBody String email) {

        User user = userService.findUserByEmail(email);

        if (user == null) {
            log.error("The email {} does not correspond to an active user", email);
            throw new RuntimeException("The email " + email + " does not correspond to an active user");
        }

        VerificationToken oldToken = verificationTokenRepository.findActiveByUserEmail(email);

        if (oldToken == null) {
            log.error("The email {} does not have an active associated token!", email);
            throw new RuntimeException("The user " + email + " does not have an active associated token!");
        }

        oldToken.setExpirationDate(LocalDateTime.from(LocalDateTime.of(2000, 11, 12, 0, 0, 0)));
        VerificationToken newToken = new VerificationToken(user);
        VerificationToken savedToken = verificationTokenRepository.save(newToken);

        URL url = null;
        try {
            url = new URL(request.getRequestURL().toString().replace("/resendVerificationLink", "") + "/registerConfirm?token=" + savedToken.getToken());
        } catch (MalformedURLException e) {
            log.error("The user is currently disabled!");
            e.printStackTrace();
        }

        String code = savedToken.getToken();
        emailServiceImpl.sendActivateAccountEmail(user, code);
    }

    @GetMapping("/registerConfirm")
    public void registerConfirm(@RequestParam("token") String token) {
        VerificationToken storedToken = verificationTokenRepository.findByToken(token);

        if (storedToken == null) {
            throw new VerificationTokenException("Invalid token provided!");
        }

        if (storedToken.isExpired()) {
            verificationTokenRepository.delete(storedToken);
            userService.delete(storedToken.getUser());
            throw new VerificationTokenException("The token is expired! Please register again!");
        }

        User registeredUser = storedToken.getUser();
        registeredUser.setEnabled(true);
        userService.save(registeredUser);

        emailServiceImpl.sendWelcomeEmail(verificationTokenRepository.findByToken(token).getUser());
        verificationTokenRepository.delete(storedToken);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'SUPER_ADMIN')")
    @GetMapping("/findVerificationTokenByEmail")
    public VerificationToken findVerificationTokenByEmail(@RequestParam("email") String email) {
        return this.verificationTokenRepository.findAll().stream().filter(verificationToken -> verificationToken.getUser().getEmail().equals(email)).findFirst().orElse(null);
    }
}
