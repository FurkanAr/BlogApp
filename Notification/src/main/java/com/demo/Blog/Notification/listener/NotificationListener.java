package com.demo.Blog.Notification.listener;

import com.demo.Blog.Notification.repository.MailRepository;
import com.demo.Blog.Notification.converter.MailConverter;
import com.demo.Blog.Notification.model.Mail;
import com.demo.Blog.Notification.request.MailRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class NotificationListener {

    private final MailRepository mailRepository;
    private final MailConverter mailConverter;

    Logger logger = LoggerFactory.getLogger(getClass());

    public NotificationListener(MailRepository mailRepository, MailConverter mailConverter) {
        this.mailRepository = mailRepository;
        this.mailConverter = mailConverter;
    }

    @RabbitListener(queues = "blog.confirmation.notification")
    public void notificationListener(MailRequest mailRequest){
        logger.info("Notification listener invoked - Consuming Message with EmailRequest Email: {}", mailRequest.getEmail());
        Mail mail = mailConverter.convert(mailRequest);
        Mail savedMail = mailRepository.save(mail);
        logger.info("Mail created: {} ", savedMail.getId());
        logger.info("notificationListener method successfully worked");
    }
}
