package yu.cse.odiga.auth.application;

import io.jsonwebtoken.Claims;

import java.io.IOException;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import yu.cse.odiga.auth.dao.RefreshTokenRepository;
import yu.cse.odiga.auth.dao.UserRepository;
import yu.cse.odiga.auth.domain.RefreshToken;
import yu.cse.odiga.auth.domain.User;
import yu.cse.odiga.auth.dto.LoginDto;
import yu.cse.odiga.auth.dto.RefreshTokenDto;
import yu.cse.odiga.auth.dto.SignUpDto;
import yu.cse.odiga.auth.exception.AlreadyExistUserException;
import yu.cse.odiga.auth.exception.TokenNotFoundException;
import yu.cse.odiga.global.jwt.JwtTokenDto;
import yu.cse.odiga.global.jwt.JwtTokenProvider;
import yu.cse.odiga.global.type.Role;
import org.springframework.beans.factory.annotation.Value;

@Service
@Transactional
@RequiredArgsConstructor
public class UserAuthService {
    @Value("${profile.default-image-url}")
    private String defaultProfileImageUrl;
    private final UserRepository userRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final PasswordEncoder passwordEncoder;
    private final RefreshTokenRepository refreshTokenRepository;
    private final S3ProfileImageUploadService s3ProfileImageUploadService;

    // TODO : DTO 잘못된 데이터 들어올 경우 에러처리

    public User signUp(SignUpDto signUpDto) {

        if (userRepository.findByEmail(signUpDto.getEmail()).isPresent()) {
            throw new AlreadyExistUserException("이미 존재하는 이메일 입니다.");
        }

        User user = User.builder()
                .email(signUpDto.getEmail())
                .nickname(signUpDto.getNickname())
                .password(passwordEncoder.encode(signUpDto.getPassword()))
                .role(Role.ROLE_USER)
                .build();

        if (signUpDto.getProfileImage() != null && !signUpDto.getProfileImage().isEmpty()) {
            try {
                s3ProfileImageUploadService.upload(signUpDto.getProfileImage(), user);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } else {
            user.setProfileImageUrl(defaultProfileImageUrl);
        }

        userRepository.save(user);

        return user;
    }

    //
//    // TODO : refresh token 으로 access token 재발급 구현.
//
    public JwtTokenDto login(LoginDto loginDto) {

        User user = userRepository.findByEmail(loginDto.getEmail())
                .orElseThrow(() -> new UsernameNotFoundException("존재하지 않는 계정 입니다."));

        if (!passwordEncoder.matches(loginDto.getPassword(), user.getPassword())) {
            throw new BadCredentialsException("비밀번호가 일치 하지 않습니다.");
        }

        JwtTokenDto jwtTokenDto = jwtTokenProvider.createToken(loginDto.getEmail());

        Optional<RefreshToken> refreshTokenInDB = refreshTokenRepository.findByUserEmail(user.getEmail());

        if (refreshTokenInDB.isPresent()) {
            refreshTokenInDB.get().setToken(jwtTokenDto.getRefreshToken());
            return jwtTokenDto;
        }

        RefreshToken refreshToken = RefreshToken.builder()
                .token(jwtTokenDto.getRefreshToken())
                .userEmail(user.getEmail())
                .build();

        refreshTokenRepository.save(refreshToken);

        return jwtTokenDto;
    }

    //
//    // TODO : Security Exception 순서에 따라 Exception 처리 변경
//
    public JwtTokenDto reIssue(RefreshTokenDto refreshTokenDto) {

        if (!jwtTokenProvider.validateToken(refreshTokenDto.getRefreshToken())) {
            throw new IllegalArgumentException("올바르지 않은 token 입니다");
        }

        Claims claims = jwtTokenProvider.getClaims(refreshTokenDto.getRefreshToken());

        String userEmail = claims.getSubject();

        RefreshToken refreshToken = refreshTokenRepository.findByUserEmail(userEmail)
                .orElseThrow(() -> new TokenNotFoundException("저장된 refresh token 을 찾을 수 없습니다. 다시 로그인 하세요"));

        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new UsernameNotFoundException("존재하지 않는 계정 입니다."));

        if (!refreshToken.getToken().equals(refreshTokenDto.getRefreshToken())) {
            throw new IllegalArgumentException("올바르지 않은 token 입니다");
        }

        JwtTokenDto jwtTokenDto = jwtTokenProvider.createToken(user.getEmail());

        refreshToken.setToken(jwtTokenDto.getRefreshToken());

        return jwtTokenDto;
    }
}