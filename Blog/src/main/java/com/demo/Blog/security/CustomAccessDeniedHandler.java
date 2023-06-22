package com.demo.Blog.security;

import com.demo.Blog.exception.response.ExceptionResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
@Component
public class CustomAccessDeniedHandler  implements AccessDeniedHandler {
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {

        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);

        ExceptionResponse exceptionResponse = new ExceptionResponse(
                accessDeniedException.getMessage(),
                HttpStatus.FORBIDDEN.value(),
                request.getServletPath());

        PrintWriter writer = response.getWriter();

        writer.println(new ObjectMapper().writeValueAsString(exceptionResponse));


    }
}
