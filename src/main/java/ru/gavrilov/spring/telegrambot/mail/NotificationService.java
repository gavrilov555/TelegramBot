package ru.gavrilov.spring.telegrambot.mail;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import ru.gavrilov.spring.telegrambot.entity.User;
import ru.gavrilov.spring.telegrambot.service.UserService;

import java.util.List;

@Component
@PropertySource("classpath:telegram.properties")
public class NotificationService {

    private final UserService userService;
    private final JavaMailSender mailSender;

    @Value("Новая заявка")
    private String mailSubject;

    @Value("${mail_from}")
    private String mailFrom;

    @Value("${mail_to}")
    private String mailTo;

    public NotificationService (UserService userService, JavaMailSender mailSender) {
        this.userService = userService;
        this.mailSender = mailSender;
    }

    @Scheduled(fixedRate = 10000)
    public void sendSubject() {
        List<User> users = userService.findNewUsers();
        if (users.size() == 0)
            return;

        StringBuilder sb = new StringBuilder();

        users.forEach(user ->
                sb.append("Имя: ")
                        .append(user.getUserName())
                        .append("\r\n")
                        .append("Телефон: ")
                        .append(user.getPhone())
                        .append("\r\n\r\n")
                        .append("Email: ")
                        .append(user.getEmail())
                        .append("\r\n\r\n")
        );

        sendEmail(sb.toString());
    }

    private void sendEmail(String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(mailTo);
        message.setFrom(mailFrom);
        message.setSubject(mailSubject);
        message.setText(text);

        mailSender.send(message);
    }
}

