package com.demo.Blog.constants;

public class Constant {

    public static class Authentication {
        public static final String LOGIN_MESSAGE = "User successfully login!!";
        public static final String REGISTRATION_MAIL_MESSAGE = "Welcome to the blog app!!";
        public static final String REGISTRATION_MESSAGE = "User successfully registered!!";

    }

    public static class Card {
        public static final String CARD_SAVED = "Card successfully saved!!";

    }

    public static class User {
        public static final String PASSWORD_UPDATED = "Your password is updated";

    }

    public static class Jwt {
        public static final long EXPIRES_IN = 1440000;
        public static final String SECRET_KEY = "9ln6jZ1h5BuP28k5RmlOaeL5rise7xe9czd4yA8pZdfGA36zbmRt";

    }

    public static class Membership {
        public static final String MEMBERSHIP_RENEWED = "Your membership renewed, Have fun!!";
        public static final String MEMBERSHIP_CREATED = "Your membership created, Have fun!!";
    }
}
