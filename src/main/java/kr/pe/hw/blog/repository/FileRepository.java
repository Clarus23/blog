package kr.pe.hw.blog.repository;

import kr.pe.hw.blog.domain.UploadFile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FileRepository extends JpaRepository<UploadFile, Long> {
}
