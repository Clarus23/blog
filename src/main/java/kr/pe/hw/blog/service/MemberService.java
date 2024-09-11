package kr.pe.hw.blog.service;

import kr.pe.hw.blog.domain.Member;
import kr.pe.hw.blog.dto.LoginRequest;
import kr.pe.hw.blog.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class MemberService {
    MemberRepository memberRepository;
    PasswordEncoder passwordEncoder;

    @Autowired
    public MemberService(MemberRepository memberRepository, PasswordEncoder passwordEncoder) {
        this.memberRepository = memberRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public Long save(Member member) {
        if(memberRepository.findByUserId(member.getUserId()).isPresent()) {
            return null;
        }

        member.setUserPwd(passwordEncoder.encode(member.getUserPwd()));
        memberRepository.save(member);
        return member.getId();
    }

    public boolean validateMember(LoginRequest loginInfo) {
        Member member = memberRepository.findByUserId(loginInfo.getUserId())
                .orElseThrow(() -> new UsernameNotFoundException("회원을 찾을 수 없습니다."));

        return passwordEncoder.matches(loginInfo.getPassword(), member.getUserPwd());
    }
}
