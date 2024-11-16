package kr.pe.hw.blog.dto;

import kr.pe.hw.blog.domain.Member;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class SignUpDto {
    private String userId;
    private String userPwd;
    private String name;
    private String email;
    private String phone;

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
