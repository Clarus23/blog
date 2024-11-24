package kr.pe.hw.blog.service;

import kr.pe.hw.blog.dto.FileUploadDto;
import kr.pe.hw.blog.repository.FileRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
public class FileService {
    @Value("${file.dir}") String filePath;
    private FileRepository fileRepository;

    @Autowired
    public FileService(FileRepository fileRepository) {
        this.fileRepository = fileRepository;
    }

    public String tempSave(MultipartFile multipartFile) {
        String fileExtension = multipartFile.getOriginalFilename().substring(multipartFile.getOriginalFilename().lastIndexOf("."));
        String newFileName = UUID.randomUUID() + fileExtension;
        File targetFile = new File(filePath + "summernote-images/temp/" + newFileName);

        try {
            InputStream fileStream = multipartFile.getInputStream();
            FileUtils.copyInputStreamToFile(fileStream, targetFile);
        } catch(IOException e) {
            FileUtils.deleteQuietly(targetFile);
            throw new RuntimeException(e);
        }

        return "/summernote-images/temp/" + newFileName;
    }

    @Transactional
    public boolean save(MultipartFile[] files, List<String> newUri) {
        LocalDate now = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM");
        String formattedNow = now.format(formatter);

        List<FileUploadDto> fileDto = new ArrayList<>();
        try {
            for(int i=0; i<files.length; i++) fileDto.add(saveFile(files[i], newUri.get(i), formattedNow));
        } catch(Exception e) {
            return false;
        }

        for(FileUploadDto upload : fileDto) {
            fileRepository.save(upload.toEntity());
        }

        return true;
    }

    private FileUploadDto saveFile(MultipartFile file, String srcPath, String formattedNow) {
        FileUploadDto dto = null;

        if(file != null) {
            String originalFileName = file.getOriginalFilename();
            String fileExtension = originalFileName.substring(originalFileName.lastIndexOf("."));
            String saveFileName = srcPath.substring(srcPath.lastIndexOf("/")+1);
            long size = file.getSize();

            String dstPath = srcPath.replaceAll("/temp/", "/" + formattedNow + "/");
//            File targetFile = new File(filePath + "summernote-images/" + formattedNow + "/" + saveFileName);
            try {
                //file Upload
                log.info("file path : {}", filePath);
                FileUtils.moveFile(new File(filePath+srcPath), new File(filePath+dstPath));
//                file.transferTo(targetFile);
                //file save
//                InputStream fileStream = file.getInputStream();
//                FileUtils.copyInputStreamToFile(fileStream, targetFile);

                dto = FileUploadDto.builder()
                        .originalFileName(originalFileName)
                        .saveFileName(saveFileName)
                        .fileExtension(fileExtension)
                        .fileSize(size)
                        .build();
                log.info("save file : {}", dstPath);
            } catch(IllegalStateException | IOException e) {
                FileUtils.deleteQuietly(new File(dstPath));
                throw new RuntimeException(e);
            }
        }
        return dto;
    }
}
