package kr.pe.hw.blog.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;


@Entity
@Getter @Setter @ToString
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    @Size(min=4, max=16)
    @Column(nullable = false, unique = true)
    String userId;
    @Size(min=8)
    @Column(nullable = false)
    String userPwd;
    @Column(nullable = false)
    String name;
    @Pattern(regexp = "^[\\w!#$%&'*+/=?`{|}~^.-]+@[\\w.-]+\\.[a-zA-Z]{2,6}$", message = "이메일 형식이 올바르지 않습니다.")
    @Column(nullable = false, unique = true)
    String eMail;
    @Pattern(regexp = "^(01[016789])-?[0-9]{3,4}-?[0-9]{4}$", message = "핸드폰 번호 형식이 올바르지 않습니다.")
    @Column(nullable = false)
    String phone;

    @Enumerated(EnumType.STRING)
    Role permissionLevel;

    public Member() {}

    public Member(Long id, String userId, String userPwd, String name, String eMail, String phone, Role permissionLevel) {
        this.id = id;
        this.userId = userId;
        this.userPwd = userPwd;
        this.name = name;
        this.eMail = eMail;
        this.phone = phone;
        this.permissionLevel = permissionLevel;
    }
}
