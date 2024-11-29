package kr.pe.hw.blog.service;

import kr.pe.hw.blog.domain.Post;
import kr.pe.hw.blog.domain.UploadFile;
import kr.pe.hw.blog.dto.PostUploadDto;
import kr.pe.hw.blog.repository.FileRepository;
import kr.pe.hw.blog.repository.PostRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@Transactional
public class PostService {
    private final PostRepository postRepository;
    private final FileRepository fileRepository;
    private final FileService fileService;

    @Autowired
    public PostService(PostRepository postRepository, FileService fileService, FileRepository fileRepository) {
        this.postRepository = postRepository;
        this.fileRepository = fileRepository;
        this.fileService = fileService;
    }

    /**
     * 게시글 등록
     * @param post
     * @return post.id
     */
    public Long save(PostUploadDto post, MultipartFile[] images, List<String> newUri) {
        String content = post.getContent();

        LocalDateTime postTime = post.getCreatedDate();
        LocalDate time;
        if(postTime == null) time = LocalDate.now();
        else time = postTime.toLocalDate();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM");
        String formattedNow = time.format(formatter);

        content = content.replaceAll("/temp/", "/" + formattedNow + "/");
        post.setContent(content);

        List<UploadFile> fileList = fileService.toUploadFile(images, newUri);
        Post savedPost = postRepository.save(post.toEntity(fileList));

        fileService.save(fileList, formattedNow, savedPost);
        return savedPost.getId();
    }

    /**
     * 게시글 수정
     * @param post
     * @return post.id
     */
    public Long update(Long id, PostUploadDto post, MultipartFile[] images, List<String> newUri, List<String> deleteFilenames) {
        Post oldPost = postRepository.findById(id).orElseThrow();
        String content = post.getContent();

        LocalDate time = oldPost.getCreatedDate().toLocalDate();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM");
        String formattedNow = time.format(formatter);

        content = content.replaceAll("/temp/", "/" + formattedNow + "/");

        oldPost.setTitle(post.getTitle());
        oldPost.setContent(content);

        fileService.delete(deleteFilenames, formattedNow);
        List<UploadFile> fileList = fileService.toUploadFile(images, newUri);
        fileService.save(fileList, formattedNow, oldPost);

        return id;
    }

    /**
     * 게시글 삭제
     * @param id
     * @return post.id
     */
    public String delete(Long id) {
        try {
            List<String> filenames = new ArrayList<>();
            for(UploadFile uploadFile : fileRepository.findAllByPostId(id)) {
                filenames.add(uploadFile.getSaveFilename());
            }
            LocalDate time = postRepository.findById(id).orElseThrow().getCreatedDate().toLocalDate();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM");
            String formattedNow = time.format(formatter);

            fileService.delete(filenames, formattedNow);

            postRepository.deleteById(id);
        } catch (EmptyResultDataAccessException e) {
            return "삭제에 실패하였습니다. 해당 게시글을 찾을 수 없습니다.";
        }

        return "해당 게시글을 삭제 하였습니다.";
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
