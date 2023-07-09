package com.demo.Blog.service;

import com.demo.Blog.client.request.PaymentCardGetRequest;
import com.demo.Blog.converter.CardConverter;
import com.demo.Blog.exception.card.CardNotFoundByUserIdException;
import com.demo.Blog.exception.card.CardNotFoundException;
import com.demo.Blog.model.CardInfo;
import com.demo.Blog.model.User;
import com.demo.Blog.model.enums.UserRole;
import com.demo.Blog.repository.CardInfoRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.Clock;
import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(SpringExtension.class)
class CardServiceTest {

    @InjectMocks
    private CardService cardService;
    @Mock
    private CardConverter cardConverter;
    @Mock
    private CardInfoRepository cardInfoRepository;

    @Test
    void it_should_save_card() {

        // given
        Mockito.when(cardConverter.convert(Mockito.any(PaymentCardGetRequest.class), Mockito.any(User.class))).thenReturn(new CardInfo());
        Mockito.when(cardInfoRepository.save(Mockito.any(CardInfo.class))).thenReturn(getCard());

        // when
        String response = cardService.saveCard(getPaymentCardGetRequest(), getUser());

        // then
        assertThat(response).isNotNull();
        assertThat(response).isEqualTo("Card successfully saved!!");

        verify(cardInfoRepository).save(Mockito.any(CardInfo.class));

    }

    @Test
    void it_should_throw_card_not_found_exception() {

        // given

        // when
        Throwable exception = catchThrowable(() -> cardService.getCardById(1L));

        // then
        assertThat(exception).isInstanceOf(CardNotFoundException.class);

        Mockito.verify(cardInfoRepository, times(0)).save(Mockito.any(CardInfo.class));
    }


    @Test
    void it_should_get_card_by_id() {

        // given
        CardInfo card = getCard();
        card.setId(1L);
        card.getUser().setId(2L);
        Mockito.when(cardInfoRepository.findById(1L)).thenReturn(Optional.of(card));

        // when
        CardInfo cardInfo = cardService.getCardById(1L);

        // then
        assertThat(cardInfo).isNotNull();
        assertThat(cardInfo.getCardNo()).isEqualTo(getCard().getCardNo());
        assertEquals(cardInfo.getId(), 1);
        assertEquals(cardInfo.getUser().getId(), 2);
    }

    @Test
    void it_should_throw_card_not_found_exception_get_card_by_user_id() {

        // given

        // when
        Throwable exception = catchThrowable(() -> cardService.getCardByUserId(2L));

        // then
        assertThat(exception).isInstanceOf(CardNotFoundByUserIdException.class);

        Mockito.verify(cardInfoRepository, times(0)).save(Mockito.any(CardInfo.class));
    }

    @Test
    void it_should_get_card_by_user_id() {

        // given
        CardInfo card = getCard();
        card.setId(1L);
        card.getUser().setId(2L);
        Mockito.when(cardInfoRepository.findByUserId(2L)).thenReturn(Optional.of(card));

        // when
        CardInfo cardInfo = cardService.getCardByUserId(2L);

        // then
        assertThat(cardInfo).isNotNull();
        assertThat(cardInfo.getCardNo()).isEqualTo(getCard().getCardNo());
        assertEquals(cardInfo.getId(), 1);
        assertEquals(cardInfo.getUser().getId(), 2);
    }


    private CardInfo getCard() {
        return new CardInfo("4441113332", LocalDate.now(Clock.systemDefaultZone()), "131", getUser());
    }

    private PaymentCardGetRequest getPaymentCardGetRequest() {
        return new PaymentCardGetRequest("4441113332", LocalDate.now(Clock.systemDefaultZone()), "131", 2L);
    }

    private User getUser() {
        return new User("tester", "test-user", "test@gmail.com", "Test-password123", UserRole.USER);
    }

}