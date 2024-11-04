package kr.pe.hw.blog.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.Pattern;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;


@Entity
@Getter @Setter @ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Member implements UserDetails {
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

//    @Enumerated(EnumType.STRING)
//    Role permissionLevel;
    @ElementCollection(fetch = FetchType.EAGER)
    @Builder.Default
    private List<String> roles = new ArrayList<>();

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.roles.stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }

    @Override
    public String getPassword() {
        return userPwd;
    }

    @Override
    public String getUsername() {
        return userId;
    }
}
