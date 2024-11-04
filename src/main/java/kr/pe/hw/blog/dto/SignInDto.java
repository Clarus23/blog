package kr.pe.hw.blog.dto;

import lombok.*;

@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SignInDto {
    private String username;
    private String password;
}
