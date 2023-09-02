package com.dizertatie.backend.user.service;

import com.dizertatie.backend.user.model.User;
import com.dizertatie.backend.user.model.UsersRentals;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@Service
public class EmailServiceImpl {

    private final JavaMailSender javaMailSender;

    @Autowired
    public EmailServiceImpl(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
        logo = new ClassPathResource("logo-black-pen.png");
        pen = new ClassPathResource("logo-black.png");
    }

    @Autowired
    private TemplateEngine templateEngine;

    private final Resource logo;
    private final Resource pen;

    public void sendWelcomeEmail(User user) {
        MimeMessagePreparator messagePreparator = mimeMessage -> {
            MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage, true);
            messageHelper.setTo(user.getEmail());
            messageHelper.setSubject("Welcome to our platform!");
            Context context = new Context();
            context.setVariable("firstName", user.getFirstName());
            context.setVariable("logo", "logo");
            context.setVariable("pen", "pen");
            String content = templateEngine.process("welcomeEmailTemplate", context);
            messageHelper.setText(content, true);
            messageHelper.addInline("logo", logo, "image/png");
            messageHelper.addInline("pen", pen, "image/png");
        };

        try {
            javaMailSender.send(messagePreparator);
        } catch (MailException e) {
            // runtime com.dizertatie.backend.game.exception; compiler will not force you to handle it
            System.out.println(e.getMessage());
        }
    }

    public void sendRentalEmail(UsersRentals usersRentals) {
        MimeMessagePreparator messagePreparator = mimeMessage -> {
            MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage, true);
            messageHelper.setTo(usersRentals.getUser().getEmail());
            messageHelper.setSubject("You just rented a game");
            Context context = new Context();
            context.setVariable("firstName", usersRentals.getUser().getFirstName());
            context.setVariable("gameTitle", usersRentals.getGame().getTitle());
            context.setVariable("returnDate", usersRentals.getReturn_date());
            context.setVariable("logo", "logo");
            context.setVariable("pen", "pen");
            String content = templateEngine.process("rentedGameTemplate", context);
            messageHelper.setText(content, true);
            messageHelper.addInline("logo", logo, "image/png");
            messageHelper.addInline("pen", pen, "image/png");
        };

        try {
            javaMailSender.send(messagePreparator);
        } catch (MailException e) {
            // runtime com.dizertatie.backend.game.exception; compiler will not force you to handle it
            System.out.println(e.getMessage());
        }
    }

    public void sendActivateAccountEmail(User user, String code) {
        MimeMessagePreparator messagePreparator = mimeMessage -> {
            MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage, true);
            messageHelper.setTo(user.getEmail());
            messageHelper.setSubject("Activate account");
            Context context = new Context();
            context.setVariable("firstName", user.getFirstName());
            context.setVariable("code", code);
            context.setVariable("logo", "logo");
            context.setVariable("pen", "pen");
            String content = templateEngine.process("activateAccountTemplate", context);
            messageHelper.setText(content, true);
            messageHelper.addInline("logo", logo, "image/png");
            messageHelper.addInline("pen", pen, "image/png");
        };
        try {
            javaMailSender.send(messagePreparator);
        } catch (MailException e) {
            System.out.println(e.getMessage());
        }

    }

    public void sendAlmostReturnDateEmail(UsersRentals usersRentals) {
        MimeMessagePreparator messagePreparator = mimeMessage -> {
            MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage, true);
            messageHelper.setTo(usersRentals.getUser().getEmail());
            messageHelper.setSubject("You must return your game!");
            Context context = new Context();
            context.setVariable("gameTitle", usersRentals.getGame().getTitle());
            context.setVariable("firstName", usersRentals.getUser().getFirstName());
            context.setVariable("logo", "logo");
            context.setVariable("pen", "pen");
            String content = templateEngine.process("almostReturnDateTemplate", context);
            messageHelper.setText(content, true);
            messageHelper.addInline("logo", logo, "image/png");
            messageHelper.addInline("pen", pen, "image/png");
        };

        try {
            javaMailSender.send(messagePreparator);
        } catch (MailException e) {
            // runtime com.dizertatie.backend.game.exception; compiler will not force you to handle it
            System.out.println(e.getMessage());
        }
    }

    public void sendResetPasswordEmail(User user, String key) {
        MimeMessagePreparator messagePreparator = mimeMessage -> {
            MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage, true);
            messageHelper.setTo(user.getEmail());
            messageHelper.setSubject("Reset password");
            Context context = new Context();
            context.setVariable("firstName", user.getFirstName());
            context.setVariable("key", key);
            context.setVariable("logo", "logo");
            context.setVariable("pen", "pen");
            String content = templateEngine.process("resetPasswordTemplate", context);
            messageHelper.setText(content, true);
            messageHelper.addInline("logo", logo, "image/png");
            messageHelper.addInline("pen", pen, "image/png");
        };

        try {
            javaMailSender.send(messagePreparator);
        } catch (MailException e) {
            // runtime com.dizertatie.backend.game.exception; compiler will not force you to handle it
            System.out.println(e.getMessage());
        }
    }
}

