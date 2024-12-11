package kr.pe.hw.blog.repository;

import kr.pe.hw.blog.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findByUserId(String userId);
    boolean existsByUserId(String userId);

    @Query("select m.id from Member m where m.userId = :name")
    Optional<Long> findIdByName(@Param("name") String name);
}
