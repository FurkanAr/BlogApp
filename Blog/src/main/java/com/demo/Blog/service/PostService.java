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
        //User user = userService.findUserById(newPost.getUserId());
        Membership membership = membershipService.getMembershipByUserId(newPost.getUserId());

        if (!MembershipUtil.isMembershipActive(membership)) {
            membershipService.deleteMembershipById(membership.getId());
            throw new MembershipIsExpiredException(Messages.Membership.EXPIRED);
        }

        List<Tag> tags = tagService.findAllById(newPost.getTagIds());

        checkUserCanCreateOrNotPost(membership.getUser().getId());

        Post post = postRepository.save(postConverter.convert(newPost, membership.getUser(), tags));


        return postConverter.convert(post);
    }

    public List<PostResponse> getAllPosts(Optional<Long> userId) {
        if (userId.isPresent())
            return postConverter.convert(postRepository.findByUserId(userId.get()));
        return postConverter.convert(postRepository.findByOrderByPublicationDateDesc());
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

    public PostUpdateResponse updatePost(Long postId, PostUpdateRequest postUpdateRequest) {
        Post post = getPostById(postId);
        Post updatedPost = postConverter.update(post, postUpdateRequest);
        return postConverter.update(postRepository.save(updatedPost));
    }

    public Post getPostById(Long postId) {
        return postRepository.findById(postId).orElseThrow(() ->
                new PostNotFoundException(Messages.Post.NOT_EXISTS_BY_ID + postId));
    }

    public String deletePostById(Long postId) {
        Post post = getPostById(postId);
        postRepository.delete(post);
        return post.getId().toString();
    }

    public void deleteByUserId(Long userId) {
        userService.findUserById(userId);
        List<Post> posts = postRepository.findAllByUserId(userId);
        postRepository.deleteAll(posts);
    }

    private void checkUserCanCreateOrNotPost(Long userId) {
        LocalDate now = LocalDate.now();
        LocalDate firstDay = now.with(TemporalAdjusters.firstDayOfMonth());
        LocalDate lastDay = now.with(TemporalAdjusters.lastDayOfMonth());

        int count = postRepository.CountAllByPublicationDateBetweenByUserId(userId, firstDay, lastDay);

        PostUtil.checkUserNumberOfPosts(count);
    }

    public List<PostResponse> getPostByTag(String tag) {
        Long tagId = tagService.findByName(tag);
        List<Post> posts = postRepository.findPostsByTagsId(tagId);
        return postConverter.convert(posts);
    }
}
