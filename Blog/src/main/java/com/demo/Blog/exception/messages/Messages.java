package com.demo.Blog.exception.messages;

public class Messages {


    public static class User {
        public static final String EXIST = "User already has account by given email: ";
        public static final String NOT_EXISTS = "User cannot find with given username: ";
        public static final String EMAIL_NOT_EXISTS = "User cannot find with given email: ";
        public static final String ID_NOT_EXISTS = "User cannot find with given id: ";
        public static final String NAME_EXIST = "Username is used : ";
        public static final String EMAIL_EXIST = "Email is used: ";
        public static final String INCORRECT_PASSWORD = "Your password is incorrect given username: ";


    }

    public static class Card {
        public static final String NOT_EXISTS_BY_ID = "Card cannot found! id: ";
        public static final String NOT_EXIST_BY_USER_ID = "Card cannot found! userId: ";

    }

    public static class Comment {
        public static final String NOT_EXISTS_BY_ID = "Comment cannot found! id: ";
        public static final String NOT_EXIST_BY_USER_ID = "Comment cannot found! userId: ";

    }

    public static class Membership {
        public static final String EXPIRED = "Membership is expired";
        public static final String NOT_EXISTS_BY_ID = "Membership cannot found! id: ";
        public static final String NOT_EXIST_BY_USER_ID = "Membership cannot found! userId: ";
        public static final String EXIST = "User has membership";
    }

    public static class Like {
        public static final String NOT_EXISTS_BY_ID = "Like cannot found! id: ";
        public static final String LIKE_EXIST = "You already like this post! postId: ";

    }

    public static class Payment {
        public static final String REFUSED = "Payment Refused! Try again.";
    }

    public static class Post {

        public static final String NOT_EXISTS_BY_ID = "Post cannot found! id: ";
        public static final String MAX_POST = "User already has 10 Post";
    }

    public static class TAG {
        public static final String EXIST = "Tag already in use: ";
        public static final String NOT_EXISTS_BY_ID = "Tag cannot found! id: ";
        public static final String NOT_EXISTS_BY_NAME = "Tag cannot found! name: ";


    }


}
