package yu.cse.odiga.auth.application;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import yu.cse.odiga.auth.dao.UserRepository;
import yu.cse.odiga.auth.domain.User;
import yu.cse.odiga.auth.dto.LoginDto;
import yu.cse.odiga.auth.dto.SignUpDto;
import yu.cse.odiga.auth.exception.AlreadyExistUserException;
import yu.cse.odiga.global.jwt.JwtTokenDto;
import yu.cse.odiga.global.jwt.JwtTokenProvider;
import yu.cse.odiga.global.type.Role;

@Service
@RequiredArgsConstructor
@Transactional
public class AuthService {

    private final UserRepository userRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final PasswordEncoder passwordEncoder;

    public JwtTokenDto signUp(SignUpDto signUpDto) {

        if (userRepository.findByEmail(signUpDto.getEmail()).isPresent()) {
            throw new AlreadyExistUserException("이미 존재하는 이메일 입니다.");
        }

        User user = User.builder()
                .email(signUpDto.getEmail())
                .nickname(signUpDto.getNickname())
                .password(passwordEncoder.encode(signUpDto.getPassword()))
                .role(Role.ROLE_USER)
                .build();

        userRepository.save(user);

        JwtTokenDto jwtTokenDto = jwtTokenProvider.createToken(user.getEmail(), user.getNickname());

        return jwtTokenDto;
    }

    // TODO : refresh token 으로 access token 재발급 구현.

    public JwtTokenDto login(LoginDto loginDto) {

        User user = userRepository.findByEmail(loginDto.getEmail())
                .orElseThrow(() -> new UsernameNotFoundException("존재하지 않는 계정 입니다."));

        if (!passwordEncoder.matches(loginDto.getPassword(), user.getPassword())) {
            throw new BadCredentialsException("비밀번호가 일치 하지 않습니다.");
        }

        JwtTokenDto jwtTokenDto = jwtTokenProvider.createToken(user.getEmail(), user.getNickname());

        return jwtTokenDto;
    }
}
