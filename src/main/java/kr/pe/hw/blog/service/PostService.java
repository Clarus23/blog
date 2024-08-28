package kr.pe.hw.blog.service;

import kr.pe.hw.blog.domain.Post;
import kr.pe.hw.blog.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class PostService {
    private final PostRepository postRepository;

    @Autowired
    public PostService(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    /**
     * 게시글 등록
     * @param post
     * @return post.id
     */
    public Long save(Post post) {
        postRepository.save(post);
        return post.getId();
    }

    /**
     * 게시글 수정
     * @param post
     * @return post.id
     */
    public Long update(Post post) {
        Post oldPost = postRepository.findById(post.getId()).orElseThrow();

        oldPost.setTitle(post.getTitle());
        oldPost.setContent(post.getContent());
        return post.getId();
    }

    /**
     * 게시글 삭제
     * @param id
     * @return post.id
     */
    public Long delete(Long id) {
        Post deletedPost = postRepository.findById(id).orElseThrow();

        postRepository.delete(deletedPost);
        return deletedPost.getId();
    }

    public Post getPost(long id) {
        return postRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Post not found"));
    }

    public Page<Post> findPostPages(int page) {
        List<Sort.Order> sorts = new ArrayList<>();
        sorts.add(Sort.Order.desc("id"));
        Pageable pageable = PageRequest.of(page, 15, Sort.by(sorts));
        return postRepository.findAll(pageable);
    }
}
