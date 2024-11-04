package kr.pe.hw.blog.dto;

import kr.pe.hw.blog.domain.Member;
import lombok.*;

@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MemberDto {
    private Long id;
    private String userId;
    private String name;
    private String email;
    private String phone;

    static public MemberDto toDto(Member member) {
        return MemberDto.builder()
                .id(member.getId())
                .userId(member.getUserId())
                .name(member.getName())
                .email(member.getEMail())
                .phone(member.getPhone())
                .build();
    }

    public Member toEntity() {
        return Member.builder()
                .id(id)
                .userId(userId)
                .name(name)
                .eMail(email)
                .phone(phone)
                .build();
    }
}
