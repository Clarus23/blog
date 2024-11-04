package kr.pe.hw.blog.dto;

import kr.pe.hw.blog.domain.Member;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SignUpDto {
    private String userId;
    private String userPwd;
    private String name;
    private String email;
    private String phone;
    private List<String> roles = new ArrayList<>();

    public Member toEntity(String encodedPassword, List<String> roles) {
        return Member.builder()
                .userId(userId)
                .userPwd(encodedPassword)
                .name(name)
                .eMail(email)
                .phone(phone)
                .roles(roles)
                .build();
    }
}
