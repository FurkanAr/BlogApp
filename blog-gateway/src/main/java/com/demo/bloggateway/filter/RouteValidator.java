package com.demo.bloggateway.filter;

import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.function.Predicate;

@Component
public class RouteValidator {

    public static final List<String> openApiEndpoints = List.of(
            "/auth/register",
            "/auth/login",
            "/posts",
            "/comments",
            "/users",
            "/likes",
            "/tags",
            "/memberships",
            "/payments",
            "/dashboard",
            "/dashboard/admin",
            "/dashboard/user");

    public Predicate<ServerHttpRequest> isSecured =
            request -> openApiEndpoints
                    .stream()
                    .noneMatch(url -> request.getURI().getPath().contains(url));


}
