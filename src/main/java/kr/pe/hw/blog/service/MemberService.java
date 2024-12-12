package kr.pe.hw.blog.service;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import kr.pe.hw.blog.config.TokenProvider;
import kr.pe.hw.blog.controller.MemberController;
import kr.pe.hw.blog.domain.Member;
import kr.pe.hw.blog.domain.Role;
import kr.pe.hw.blog.dto.MemberDto;
import kr.pe.hw.blog.dto.SignInDto;
import kr.pe.hw.blog.dto.SignUpDto;
import kr.pe.hw.blog.dto.TokenDto;
import kr.pe.hw.blog.repository.MemberRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.User.UserBuilder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional(readOnly = true)
@Slf4j
public class MemberService implements UserDetailsService{
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final TokenProvider tokenProvider;

    @Autowired
    public MemberService(MemberRepository memberRepository, PasswordEncoder passwordEncoder, TokenProvider tokenProvider, AuthenticationManagerBuilder authenticationManagerBuilder) {
        this.memberRepository = memberRepository;
        this.passwordEncoder = passwordEncoder;
        this.tokenProvider = tokenProvider;
        this.authenticationManagerBuilder = authenticationManagerBuilder;
    }

    @Transactional
    public TokenDto signIn(SignInDto signInDto) {

        //1. username + password 를 기반으로 Authentication 객체 생성
        //이때 authentication 은 인증 여부를 확인하는 authenticated 값이 false
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(signInDto.getUsername(), signInDto.getPassword());

        //2. 실제 검증. authendticate() 메서드를 통해 요청된 Member 에 대한 검증 진행
        // authenticate 메서드가 실행될 때 CustomUserDetailService 에서 만든 loadUserByUsername 메서드 실행.
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);

        //3. 인증 정보를 기반으로 JWT 토큰 생성
        return tokenProvider.generateToken(authentication);
    }

    @Override
    public UserDetails loadUserByUsername(String userId) throws UsernameNotFoundException {
        return memberRepository.findByUserId(userId)
                .map(this::createUserDetails)
                .orElseThrow(() -> new UsernameNotFoundException("해당하는 회원을 찾을 수 없습니다."));
    }

    //해당하는 User의 데이터가 존재한다면 UserDeatils객체로 만들어서 return
    private UserDetails createUserDetails(Member member) {
        return User.builder()
                .username(member.getUserId())
                .password(member.getPassword())
                .roles(member.getRoles().toArray(new String[0]))
                .build();
    }

    // 회원가입 메서드
    @Transactional
    public void signUp(SignUpDto signUpDto) {
        if(memberRepository.existsByUserId(signUpDto.getUserId())) {
            throw new IllegalArgumentException("이미 사용중인 ID 입니다.");
        }
        // Password 암호화
        String encodedPassword = passwordEncoder.encode(signUpDto.getUserPwd());
        List<String> roles = new ArrayList<>();
        roles.add("USER"); // user권한 부여
        memberRepository.save(signUpDto.toEntity(encodedPassword, roles));
    }

    public Long findUserId(String userName) {
        return memberRepository.findIdByName(userName).orElseThrow();
    }

    public boolean validatedAccess(String postCreator, String accessUser, String accessUserRole) {
        if(postCreator.equals(accessUser) || accessUserRole.equals("ADMIN") || accessUserRole.equals("SUPER")) return true;

        return false;
    }
}
