package kr.pe.hw.blog.service;

import kr.pe.hw.blog.domain.Post;
import kr.pe.hw.blog.domain.UploadFile;
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

    public void tempDelete(String fileName) {
        try {
            File file = new File(filePath + "summernote-images/temp/" + fileName);
            if(!file.isFile()) return;

            FileUtils.delete(file);
        } catch(IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public void delete(List<String> fileNames, String formattedNow) {
        try {
            for(String fileName : fileNames) {
                File file = new File(filePath + "summernote-images/" + formattedNow + "/" + fileName);
                if(!file.isFile()) continue;

                fileRepository.deleteBySaveFilename(fileName);
                FileUtils.delete(file);
            }
        } catch(IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public List<UploadFile> toUploadFile(MultipartFile[] multipartFiles, List<String> srcPath) {
        List<UploadFile> fileList = new ArrayList<>();
        for(int i=0; i<multipartFiles.length; i++) {
            String originalFileName = multipartFiles[i].getOriginalFilename();
            String fileExtension = originalFileName.substring(originalFileName.lastIndexOf("."));
            String saveFileName = srcPath.get(i).substring(srcPath.get(i).lastIndexOf("/")+1);
            long size = multipartFiles[i].getSize();

            FileUploadDto dto = FileUploadDto.builder()
                    .originalFileName(originalFileName)
                    .saveFileName(saveFileName)
                    .fileExtension(fileExtension)
                    .fileSize(size)
                    .build();

            fileList.add(dto.toEntity());
        }

        return fileList;
    }

    @Transactional
    public boolean save(List<UploadFile> fileList, String formattedNow, Post post) {
        List<FileUploadDto> fileDto = new ArrayList<>();
        try {
            for(UploadFile file : fileList) {
                try {
                    File targetFile = new File(filePath + "summernote-images/temp/" + file.getSaveFilename());
                    File dstFile = new File(filePath + "summernote-images/" + formattedNow + "/" + file.getSaveFilename());

                    FileUtils.moveFile(targetFile, dstFile);
                } catch(IOException e) {
                    FileUtils.deleteQuietly(new File(filePath + "summernote-images/" + formattedNow + "/" + file.getSaveFilename()));
                    throw new RuntimeException(e);
                }

                file.setPost(post);
                fileRepository.save(file);
            }
        } catch(Exception e) {
            return false;
        }

        for(FileUploadDto upload : fileDto) {
            fileRepository.save(upload.toEntity());
        }

        return true;
    }

//    @Transactional
//    public boolean save(MultipartFile[] files, List<String> newUri, String formattedNow, Long postId) {
//        List<FileUploadDto> fileDto = new ArrayList<>();
//        try {
//            for(int i=0; i<files.length; i++) {
//                FileUploadDto curFile = saveFile(files[i], newUri.get(i), formattedNow, postId);
//                if(curFile == null) continue;
//
//                fileDto.add(curFile);
//            }
//        } catch(Exception e) {
//            return false;
//        }
//
//        for(FileUploadDto upload : fileDto) {
//            fileRepository.save(upload.toEntity());
//        }
//
//        return true;
//    }

//    private FileUploadDto saveFile(MultipartFile file, String srcPath, String formattedNow, Long postId) {
//        FileUploadDto dto = null;
//
//        if(file != null) {
//            String originalFileName = file.getOriginalFilename();
//            String fileExtension = originalFileName.substring(originalFileName.lastIndexOf("."));
//            String saveFileName = srcPath.substring(srcPath.lastIndexOf("/")+1);
//            long size = file.getSize();
//
//            String dstPath = srcPath.replaceAll("/temp/", "/" + formattedNow + "/");
////            File targetFile = new File(filePath + "summernote-images/" + formattedNow + "/" + saveFileName);
//            try {
//                File targetFile = new File(filePath + srcPath);
//                if(!targetFile.isFile()) return null;
//                File dstFile = new File(filePath + dstPath);
//                //file Upload
//                log.info("file path : {}", filePath);
//                FileUtils.moveFile(targetFile, dstFile);
////                file.transferTo(targetFile);
//                //file save
////                InputStream fileStream = file.getInputStream();
////                FileUtils.copyInputStreamToFile(fileStream, targetFile);
//
//                dto = FileUploadDto.builder()
//                        .originalFileName(originalFileName)
//                        .saveFileName(saveFileName)
//                        .fileExtension(fileExtension)
//                        .fileSize(size)
//                        .postId(postId)
//                        .build();
//                log.info("save file : {}", dstPath);
//            } catch(IllegalStateException | IOException e) {
//                FileUtils.deleteQuietly(new File(dstPath));
//                throw new RuntimeException(e);
//            }
//        }
//        return dto;
//    }
}
