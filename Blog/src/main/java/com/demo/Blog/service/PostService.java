package com.demo.Blog.service;

import com.demo.Blog.converter.PostConverter;
import com.demo.Blog.exception.membership.MembershipIsExpiredException;
import com.demo.Blog.exception.messages.Messages;
import com.demo.Blog.exception.post.PostNotFoundException;
import com.demo.Blog.model.Membership;
import com.demo.Blog.model.Post;
import com.demo.Blog.model.Tag;
import com.demo.Blog.repository.PostRepository;
import com.demo.Blog.request.PostRequest;
import com.demo.Blog.request.PostUpdateRequest;
import com.demo.Blog.response.PostResponse;
import com.demo.Blog.response.PostUpdateResponse;
import com.demo.Blog.utils.MembershipUtil;
import com.demo.Blog.utils.PostUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.List;
import java.util.Optional;

@Service
public class PostService {

    private final PostRepository postRepository;
    private final PostConverter postConverter;
    private final UserService userService;
    private final MembershipService membershipService;
    private final TagService tagService;

    Logger logger = LoggerFactory.getLogger(getClass());

    public PostService(PostRepository postRepository, PostConverter postConverter, UserService userService, MembershipService membershipService, TagService tagService) {
        this.postRepository = postRepository;
        this.postConverter = postConverter;
        this.userService = userService;
        this.membershipService = membershipService;
        this.tagService = tagService;
    }

    @Transactional
    public PostResponse createPost(PostRequest newPost) {
        logger.info("createPost method started");
        logger.info("Post request: {} ", newPost);

        Membership membership = membershipService.getMembershipByUserId(newPost.getUserId());

        if (MembershipUtil.isMembershipActive(membership)) {
            membershipService.deleteMembershipById(membership.getId());
            throw new MembershipIsExpiredException(Messages.Membership.EXPIRED);
        }

        List<Tag> tags = tagService.findAllById(newPost.getTagIds());

        checkUserCanCreateOrNotPost(membership.getUser().getId());

        Post post = postRepository.save(postConverter.convert(newPost, membership.getUser(), tags));
        logger.info("Post created: {} ", post.getId());

        logger.info("createPost method successfully worked");
        return postConverter.convert(post);
    }

    public List<PostResponse> getAllPosts(Optional<Long> userId) {
        logger.info("getAllPosts method started");
        if (userId.isPresent()) {
            List<Post> posts = postRepository.findByUserId(userId.get());
            logger.info("Posts for user: {} ", userId.get());
            return postConverter.convert(posts);
        }
        List<Post> posts = postRepository.findByOrderByPublicationDateDesc();
        logger.info("Posts descending order by date");
        logger.info("getAllPosts method successfully worked");
        return postConverter.convert(posts);
    }

    public List<PostResponse> getAllThisMonthPosts() {
        logger.info("getAllThisMonthPosts method started");

        LocalDate now = LocalDate.now();
        LocalDate firstDay = now.with(TemporalAdjusters.firstDayOfMonth());
        LocalDate lastDay = now.with(TemporalAdjusters.lastDayOfMonth());

        List<Post> posts = postRepository.findAllByPublicationDateBetween(firstDay, lastDay);
        logger.info("First day of month: {}, Last day of month: {}", firstDay, lastDay);

        logger.info("getAllThisMonthPosts method successfully worked");
        return postConverter.convert(posts);
    }

    public PostResponse getOnePostById(Long postId) {
        logger.info("getOnePostById method started");
        logger.info("getOnePostById method successfully worked");
        return postConverter.convert(getPostById(postId));
    }

    @Transactional
    public PostUpdateResponse updatePost(Long postId, PostUpdateRequest postUpdateRequest) {
        logger.info("updatePost method started");

        Post post = getPostById(postId);
        Post updatedPost = postConverter.update(post, postUpdateRequest);
        Post savedPost= postRepository.save(updatedPost);
        logger.info("Post updated: {} ", postId);

        logger.info("updatePost method successfully worked");
        return postConverter.update(savedPost);
    }

    public Post getPostById(Long postId) {
        logger.info("getPostById method started");

        Post post = postRepository.findById(postId).orElseThrow(() ->
                new PostNotFoundException(Messages.Post.NOT_EXISTS_BY_ID + postId));
        logger.info("Found post by postId: {} ", postId);

        logger.info("getPostById method successfully worked");
        return post;
    }

    public String deletePostById(Long postId) {
        logger.info("deletePostById method started");

        Post post = getPostById(postId);
        postRepository.delete(post);
        logger.info("Post deleted: {} ", postId);

        logger.info("deletePostById method successfully worked");
        return post.getId().toString();
    }

    public void deleteByUserId(Long userId) {
        logger.info("deleteByUserId method started");

        userService.findUserById(userId);
        List<Post> posts = postRepository.findAllByUserId(userId);
        postRepository.deleteAll(posts);
        logger.info("User: {}, all posts deleted ", userId);

        logger.info("deleteByUserId method successfully worked");
    }

    private void checkUserCanCreateOrNotPost(Long userId) {
        logger.info("checkUserCanCreateOrNotPost method started");

        LocalDate now = LocalDate.now();
        LocalDate firstDay = now.with(TemporalAdjusters.firstDayOfMonth());
        LocalDate lastDay = now.with(TemporalAdjusters.lastDayOfMonth());
        logger.info("First day of month: {}, Last day of month: {}", firstDay, lastDay);

        int count = postRepository.CountAllByPublicationDateBetweenByUserId(userId, firstDay, lastDay);
        logger.info("User: {}, number of posts in this month: {}", userId, count);

        logger.info("checkUserCanCreateOrNotPost method successfully worked");
        PostUtil.checkUserNumberOfPosts(count);
    }

    public List<PostResponse> getPostByTag(String tag) {
        logger.info("getPostByTag method started");
        Long tagId = tagService.findByName(tag);
        List<Post> posts = postRepository.findPostsByTagsId(tagId);
        logger.info("Found posts by tag: {}", tag);
        logger.info("getPostByTag method successfully worked");
        return postConverter.convert(posts);
    }
}
