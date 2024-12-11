package kr.pe.hw.blog.service;

import kr.pe.hw.blog.domain.PostComment;
import kr.pe.hw.blog.dto.PostCommentDto;
import kr.pe.hw.blog.repository.MemberRepository;
import kr.pe.hw.blog.repository.PostCommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class PostCommentService {
    private PostCommentRepository postCommentRepository;
    private MemberService memberService;

    @Autowired
    public PostCommentService(PostCommentRepository postCommentRepository, MemberService memberService) {
        this.postCommentRepository = postCommentRepository;
        this.memberService = memberService;
    }

    public List<PostCommentDto> viewComments(Long postId) {
        List<PostComment> comments = postCommentRepository.findByPostId(postId);

        List<PostCommentDto> commentDtos = new ArrayList<>();
        for (PostComment comment : comments)
            commentDtos.add(PostCommentDto.toDto(comment));

        return commentDtos;
    }

    public void save(Long postId, String comment, String userName) {
        PostComment postComment = new PostComment();

        try {
            postComment.setContent(comment);
            postComment.setCreatorId(memberService.findUserId(userName));
            postComment.setCreatorName(userName);
            postComment.setPostId(postId);
        } catch(Exception e) {
            e.printStackTrace();
        }

        postCommentRepository.save(postComment);
    }
}
