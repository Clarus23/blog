package kr.pe.hw.blog.domain;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter @Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Post extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private String content;
    private String creatorId;

    @OneToMany(mappedBy="id", fetch=FetchType.LAZY, cascade=CascadeType.REMOVE)
    @PrimaryKeyJoinColumn
    private List<UploadFile> files;
}
