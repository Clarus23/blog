package kr.pe.hw.blog.repository;

import kr.pe.hw.blog.domain.UploadFile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FileRepository extends JpaRepository<UploadFile, Long> {
    void deleteBySaveFilename(String filename);
    List<UploadFile> findAllByPostId(Long postId);
}
