package com.demo.Blog.Notification.listener;

import com.demo.Blog.Notification.converter.MailConverter;
import com.demo.Blog.Notification.model.Mail;
import com.demo.Blog.Notification.repository.MailRepository;
import com.demo.Blog.Notification.request.MailRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.times;

@ExtendWith(SpringExtension.class)
class NotificationListenerTest {

    @InjectMocks
    private NotificationListener listener;
    @Mock
    private MailRepository mailRepository;
    @Mock
    private MailConverter mailConverter;

    @Test
    void it_should_listens_notifications() {

        // given
        Mockito.when(mailConverter.convert(Mockito.any(MailRequest.class))).thenReturn(new Mail());
        Mockito.when(mailRepository.save(Mockito.any(Mail.class))).thenReturn(getMail());

        // when
        listener.notificationListener(getMailRequest());


        // then
        assertEquals(getMail().getUserName(), getMailRequest().getUserName());
        assertEquals(getMail().getMessage(), getMailRequest().getMessage());
        Mockito.verify(mailRepository, times(1)).save(Mockito.any(Mail.class));


    }

    private MailRequest getMailRequest() {
        return new MailRequest( "tester", "test@gmail.com",  "test user","test message");

    }

    private Mail getMail() {
        return new Mail("1", "tester", "test user", "test@gmail.com", "test message", LocalDateTime.now());
    }
}