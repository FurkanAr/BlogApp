package com.demo.Blog.utils;

import com.demo.Blog.exception.messages.Messages;
import com.demo.Blog.exception.post.UserHasMaximumNumberOfPostException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PostUtil {
    private static final int COUNT = 10;
    static Logger logger = LoggerFactory.getLogger(PostUtil.class);

    private PostUtil() {

    }

    public static void checkUserNumberOfPosts(int number) {
        logger.info("checkUserNumberOfPosts method started");

        if (number >= COUNT) {
            logger.warn("Number of posts: {}, maximum number of posts: {}", number, COUNT);
            throw new UserHasMaximumNumberOfPostException(Messages.Post.MAX_POST);
        }

        logger.info("Number of posts: {}", number);
        logger.info("checkUserNumberOfPosts method successfully worked");
    }

}
