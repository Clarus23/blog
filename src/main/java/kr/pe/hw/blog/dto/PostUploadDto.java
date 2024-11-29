package kr.pe.hw.blog.dto;

import kr.pe.hw.blog.domain.Post;
import kr.pe.hw.blog.domain.UploadFile;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.List;

@Getter @Setter @ToString
@Builder
public class PostUploadDto {
    private Long id;
    private String title;
    private String content;
    private String creatorId;
    private LocalDateTime createdDate;

    static public PostUploadDto toDto(Post post) {
        return PostUploadDto.builder()
                .id(post.getId())
                .title(post.getTitle())
                .content(post.getContent())
                .creatorId(post.getCreatorId())
                .createdDate(post.getCreatedDate())
                .build();
    }

    public Post toEntity(List<UploadFile> files) {
        return Post.builder()
                .id(id)
                .title(title)
                .content(content)
                .creatorId(creatorId)
                .files(files)
                .build();
    }
}
