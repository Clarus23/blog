package kr.pe.hw.blog.dto;

import kr.pe.hw.blog.domain.UploadFile;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter @ToString
@Builder
public class FileUploadDto {
    private Long id;
    private String originalFileName;
    private String saveFileName;
    private String fileExtension;
    private Long fileSize;

    static public FileUploadDto toDto(UploadFile file) {
        return FileUploadDto.builder()
                .id(file.getId())
                .originalFileName(file.getOriginalFilename())
                .saveFileName(file.getSaveFilename())
                .fileExtension(file.getFileExtension())
                .fileSize(file.getFileSize())
                .build();
    }

    public UploadFile toEntity() {
        return UploadFile.builder()
                .id(id)
                .originalFilename(originalFileName)
                .saveFilename(saveFileName)
                .contentType(fileExtension)
//                .contentType()
                .fileExtension(fileExtension)
                .fileSize(fileSize)
                .build();
    }
}
