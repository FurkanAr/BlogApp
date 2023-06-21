package com.demo.Blog.service;

import com.demo.Blog.converter.PostConverter;
import com.demo.Blog.model.Membership;
import com.demo.Blog.model.Post;
import com.demo.Blog.model.Tag;
import com.demo.Blog.model.User;
import com.demo.Blog.repository.PostRepository;
import com.demo.Blog.request.PostRequest;
import com.demo.Blog.request.PostUpdateRequest;
import com.demo.Blog.response.PostResponse;
import com.demo.Blog.utils.MembershipUtil;
import org.springframework.stereotype.Service;

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

    public PostService(PostRepository postRepository, PostConverter postConverter, UserService userService, MembershipService membershipService, TagService tagService) {
        this.postRepository = postRepository;
        this.postConverter = postConverter;
        this.userService = userService;
        this.membershipService = membershipService;
        this.tagService = tagService;
    }

    public PostResponse createPost(PostRequest newPost) {
        Membership membership = membershipService.getMembershipByUserId(newPost.getUserId());
        User user = userService.findUserById(newPost.getUserId());
        List<Tag> tags = tagService.findAllById(newPost.getTagIds());

        LocalDate now = LocalDate.now();
        LocalDate firstDay = now.with(TemporalAdjusters.firstDayOfMonth());
        LocalDate lastDay = now.with(TemporalAdjusters.lastDayOfMonth());

        if (!MembershipUtil.isMembershipActive(membership)) {
            membershipService.deleteMembershipById(membership.getId());
            throw new RuntimeException("Membership is expired");
        }
        ;
        // int count = postRepository.findAllByPublicationDateBetweenByUserId(user.getId(), firstDay, lastDay).size();
        int count = postRepository.CountAllByPublicationDateBetweenByUserId(user.getId(), firstDay, lastDay);
        System.out.println("Number of post count: " + count);

        if (count >= 10)
            throw new RuntimeException("User already has 10 post");

        Post post = postRepository.save(postConverter.convert(newPost, user, tags));

        int afterCount = postRepository.CountAllByPublicationDateBetweenByUserId(user.getId(), firstDay, lastDay);

        System.out.println("Number of post afterCount: " + afterCount);

        return postConverter.convert(post);
    }

    public List<PostResponse> getAllPosts(Optional<Long> userId) {
        if (userId.isPresent())
            return postConverter.convert(postRepository.findByUserId(userId.get()));
        return postConverter.convert(postRepository.findAll());
    }

    public List<PostResponse> getAllThisMonthPosts() {
        LocalDate now = LocalDate.now();
        LocalDate firstDay = now.with(TemporalAdjusters.firstDayOfMonth());
        LocalDate lastDay = now.with(TemporalAdjusters.lastDayOfMonth());
        return postConverter.convert(postRepository.findAllByPublicationDateBetween(firstDay, lastDay));
    }

    public PostResponse getOnePostById(Long postId) {
        return postConverter.convert(getPostById(postId));
    }

    public PostResponse updatePost(Long postId, PostUpdateRequest postUpdateRequest) {
        Post post = getPostById(postId);
        post.setText(postUpdateRequest.getText());
        post.setTitle(postUpdateRequest.getTitle());
        post.setPicture(postUpdateRequest.getPicture());
        return postConverter.convert(postRepository.save(post));
    }

    public Post getPostById(Long postId) {
        return postRepository.findById(postId).orElse(null);
    }

    public String deletePostById(Long postId) {
        Post post = getPostById(postId);
        postRepository.delete(post);
        return post.getId().toString();
    }

    public void deleteByUserId(Long userId) {
        List<Post> posts = postRepository.findAllByUserId(userId);
        postRepository.deleteAll(posts);
    }

    public List<PostResponse> getPostByTag(String tag) {
        Long tagId = tagService.findByName(tag);
        List<Post> posts = postRepository.findPostsByTagsId(tagId);
        return postConverter.convert(posts);
    }
}
