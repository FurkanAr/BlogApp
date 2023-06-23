package com.demo.Blog.controller.advice;

import com.demo.Blog.exception.card.CardNotFoundByUserIdException;
import com.demo.Blog.exception.card.CardNotFoundException;
import com.demo.Blog.exception.comment.CommentNotFoundException;
import com.demo.Blog.exception.like.LikeNotFoundException;
import com.demo.Blog.exception.like.UserAlreadyLikedException;
import com.demo.Blog.exception.membership.MembershipIsExpiredException;
import com.demo.Blog.exception.membership.MembershipNotFoundByUserIdException;
import com.demo.Blog.exception.membership.MembershipNotFoundException;
import com.demo.Blog.exception.membership.UserHasMembershipException;
import com.demo.Blog.exception.payment.PaymentRefusedException;
import com.demo.Blog.exception.post.PostNotFoundException;
import com.demo.Blog.exception.post.UserHasMaximumNumberOfPostException;
import com.demo.Blog.exception.response.ExceptionResponse;
import com.demo.Blog.exception.response.ExceptionValidatorResponse;
import com.demo.Blog.exception.tag.TagAlreadyInUseException;
import com.demo.Blog.exception.tag.TagNotFoundException;
import com.demo.Blog.exception.tag.TagNotFoundGivenTagNameException;
import com.demo.Blog.exception.user.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ExceptionValidatorResponse> handle(MethodArgumentNotValidException exception, HttpServletRequest request){
        List<String> errors = exception.getBindingResult().getFieldErrors().stream()
                        .map(FieldError :: getDefaultMessage).collect(Collectors.toList());
        return ResponseEntity
                .ok(new ExceptionValidatorResponse(
                        "Validation Failed",
                        HttpStatus.BAD_REQUEST.value(),
                        request.getServletPath(),
                        errors));
    }

    @ExceptionHandler(UserEmailAlreadyInUseException.class)
    public ResponseEntity<ExceptionResponse> handle(UserEmailAlreadyInUseException exception, HttpServletRequest request) {
        return ResponseEntity
                .ok(new ExceptionResponse(exception.getMessage(),
                        HttpStatus.BAD_REQUEST.value(),
                        request.getServletPath()));
    }

    @ExceptionHandler(CardNotFoundException.class)
    public ResponseEntity<ExceptionResponse> handle(CardNotFoundException exception, HttpServletRequest request) {
        return ResponseEntity
                .ok(new ExceptionResponse(exception.getMessage(),
                        HttpStatus.BAD_REQUEST.value(),
                        request.getServletPath()));
    }

    @ExceptionHandler(CardNotFoundByUserIdException.class)
    public ResponseEntity<ExceptionResponse> handle(CardNotFoundByUserIdException exception, HttpServletRequest request) {
        return ResponseEntity
                .ok(new ExceptionResponse(exception.getMessage(),
                        HttpStatus.BAD_REQUEST.value(),
                        request.getServletPath()));
    }

    @ExceptionHandler(CommentNotFoundException.class)
    public ResponseEntity<ExceptionResponse> handle(CommentNotFoundException exception, HttpServletRequest request) {
        return ResponseEntity
                .ok(new ExceptionResponse(exception.getMessage(),
                        HttpStatus.BAD_REQUEST.value(),
                        request.getServletPath()));
    }

    @ExceptionHandler(LikeNotFoundException.class)
    public ResponseEntity<ExceptionResponse> handle(LikeNotFoundException exception, HttpServletRequest request) {
        return ResponseEntity
                .ok(new ExceptionResponse(exception.getMessage(),
                        HttpStatus.BAD_REQUEST.value(),
                        request.getServletPath()));
    }

    @ExceptionHandler(UserAlreadyLikedException.class)
    public ResponseEntity<ExceptionResponse> handle(UserAlreadyLikedException exception, HttpServletRequest request) {
        return ResponseEntity
                .ok(new ExceptionResponse(exception.getMessage(),
                        HttpStatus.BAD_REQUEST.value(),
                        request.getServletPath()));
    }

    @ExceptionHandler(PaymentRefusedException.class)
    public ResponseEntity<ExceptionResponse> handle(PaymentRefusedException exception, HttpServletRequest request) {
        return ResponseEntity
                .ok(new ExceptionResponse(exception.getMessage(),
                        HttpStatus.BAD_REQUEST.value(),
                        request.getServletPath()));
    }

    @ExceptionHandler(MembershipIsExpiredException.class)
    public ResponseEntity<ExceptionResponse> handle(MembershipIsExpiredException exception, HttpServletRequest request) {
        return ResponseEntity
                .ok(new ExceptionResponse(exception.getMessage(),
                        HttpStatus.BAD_REQUEST.value(),
                        request.getServletPath()));
    }

    @ExceptionHandler(MembershipNotFoundByUserIdException.class)
    public ResponseEntity<ExceptionResponse> handle(MembershipNotFoundByUserIdException exception, HttpServletRequest request) {
        return ResponseEntity
                .ok(new ExceptionResponse(exception.getMessage(),
                        HttpStatus.BAD_REQUEST.value(),
                        request.getServletPath()));
    }

    @ExceptionHandler(MembershipNotFoundException.class)
    public ResponseEntity<ExceptionResponse> handle(MembershipNotFoundException exception, HttpServletRequest request) {
        return ResponseEntity
                .ok(new ExceptionResponse(exception.getMessage(),
                        HttpStatus.BAD_REQUEST.value(),
                        request.getServletPath()));
    }

    @ExceptionHandler(UserHasMembershipException.class)
    public ResponseEntity<ExceptionResponse> handle(UserHasMembershipException exception, HttpServletRequest request) {
        return ResponseEntity
                .ok(new ExceptionResponse(exception.getMessage(),
                        HttpStatus.BAD_REQUEST.value(),
                        request.getServletPath()));
    }

    @ExceptionHandler(UserHasMaximumNumberOfPostException.class)
    public ResponseEntity<ExceptionResponse> handle(UserHasMaximumNumberOfPostException exception, HttpServletRequest request) {
        return ResponseEntity
                .ok(new ExceptionResponse(exception.getMessage(),
                        HttpStatus.BAD_REQUEST.value(),
                        request.getServletPath()));
    }

    @ExceptionHandler(PostNotFoundException.class)
    public ResponseEntity<ExceptionResponse> handle(PostNotFoundException exception, HttpServletRequest request) {
        return ResponseEntity
                .ok(new ExceptionResponse(exception.getMessage(),
                        HttpStatus.BAD_REQUEST.value(),
                        request.getServletPath()));
    }

    @ExceptionHandler(TagNotFoundGivenTagNameException.class)
    public ResponseEntity<ExceptionResponse> handle(TagNotFoundGivenTagNameException exception, HttpServletRequest request) {
        return ResponseEntity
                .ok(new ExceptionResponse(exception.getMessage(),
                        HttpStatus.BAD_REQUEST.value(),
                        request.getServletPath()));
    }

    @ExceptionHandler(TagNotFoundException.class)
    public ResponseEntity<ExceptionResponse> handle(TagNotFoundException exception, HttpServletRequest request) {
        return ResponseEntity
                .ok(new ExceptionResponse(exception.getMessage(),
                        HttpStatus.BAD_REQUEST.value(),
                        request.getServletPath()));
    }

    @ExceptionHandler(TagAlreadyInUseException.class)
    public ResponseEntity<ExceptionResponse> handle(TagAlreadyInUseException exception, HttpServletRequest request) {
        return ResponseEntity
                .ok(new ExceptionResponse(exception.getMessage(),
                        HttpStatus.BAD_REQUEST.value(),
                        request.getServletPath()));
    }

    @ExceptionHandler(UserEmailNotFoundException.class)
    public ResponseEntity<ExceptionResponse> handle(UserEmailNotFoundException exception, HttpServletRequest request) {
        return ResponseEntity
                .ok(new ExceptionResponse(exception.getMessage(),
                        HttpStatus.BAD_REQUEST.value(),
                        request.getServletPath()));
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ExceptionResponse> handle(UserNotFoundException exception, HttpServletRequest request) {
        return ResponseEntity
                .ok(new ExceptionResponse(exception.getMessage(),
                        HttpStatus.BAD_REQUEST.value(),
                        request.getServletPath()));
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<ExceptionResponse> handle(UsernameNotFoundException exception, HttpServletRequest request) {
        return ResponseEntity
                .ok(new ExceptionResponse(exception.getMessage(),
                        HttpStatus.BAD_REQUEST.value(),
                        request.getServletPath()));
    }

    @ExceptionHandler(UserNameAlreadyInUseException.class)
    public ResponseEntity<ExceptionResponse> handle(UserNameAlreadyInUseException exception, HttpServletRequest request) {
        return ResponseEntity
                .ok(new ExceptionResponse(exception.getMessage(),
                        HttpStatus.BAD_REQUEST.value(),
                        request.getServletPath()));
    }
    @ExceptionHandler(PasswordNotCorrectException.class)
    public ResponseEntity<ExceptionResponse> handle(PasswordNotCorrectException exception, HttpServletRequest request) {
        return ResponseEntity
                .ok(new ExceptionResponse(exception.getMessage(),
                        HttpStatus.BAD_REQUEST.value(),
                        request.getServletPath()));
    }
}
