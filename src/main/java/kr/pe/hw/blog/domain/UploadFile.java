package kr.pe.hw.blog.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter @Setter @ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UploadFile extends BaseTimeEntity{
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String originalFilename;
    @Column(nullable = false)
    private String saveFilename;
    @Column(nullable = false)
    private String contentType;
    @Column(nullable = false)
    private String fileExtension;
    @Column(nullable = false)
    private long fileSize;
}
