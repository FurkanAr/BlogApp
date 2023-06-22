package com.demo.Blog.utils;

import com.demo.Blog.exception.messages.Messages;
import com.demo.Blog.exception.post.UserHasMaximumNumberOfPostException;
import org.springframework.beans.factory.annotation.Value;

import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;

public class PostUtil {

    private static int COUNT=10;

    private PostUtil(){

    }

    public static void checkUserNumberOfPosts(int number){
        if (number >= COUNT)
            throw new UserHasMaximumNumberOfPostException(Messages.Post.MAX_POST);
    }

}
