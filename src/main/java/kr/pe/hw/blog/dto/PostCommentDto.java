package kr.pe.hw.blog.dto;

import kr.pe.hw.blog.domain.PostComment;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter @Setter @ToString
@Builder
public class PostCommentDto {
    private Long id;
    private String content;
    private String author;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    static public PostCommentDto toDto(PostComment postComment) {
        return PostCommentDto.builder()
                .id(postComment.getId())
                .content(postComment.getContent())
                .author(postComment.getCreatorName())
                .createdAt(postComment.getCreatedDate())
                .updatedAt(postComment.getModifiedDate())
                .build();
    }

    public PostComment toEntity(Long creatorId) {
        return PostComment.builder()
                .id(id)
                .content(content)
                .creatorId(creatorId)
                .build();
    }
}
