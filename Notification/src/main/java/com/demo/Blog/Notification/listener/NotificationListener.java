package com.demo.Blog.Notification.listener;

import com.demo.Blog.Notification.repository.MailRepository;
import com.demo.Blog.Notification.converter.MailConverter;
import com.demo.Blog.Notification.model.Mail;
import com.demo.Blog.Notification.request.MailRequest;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class NotificationListener {

    private final MailRepository mailRepository;
    private final MailConverter mailConverter;


    public NotificationListener(MailRepository mailRepository, MailConverter mailConverter) {
        this.mailRepository = mailRepository;
        this.mailConverter = mailConverter;
    }

    @RabbitListener(queues = "blog.confirmation.notification")
    public void notifcationListener(MailRequest mailRequest){
        Mail mail = mailConverter.convert(mailRequest);
        mailRepository.save(mail);

    }
}
