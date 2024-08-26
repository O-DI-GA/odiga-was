package yu.cse.odiga.owner.application;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import yu.cse.odiga.global.exception.BusinessLogicException;
import yu.cse.odiga.global.jwt.JwtTokenDto;
import yu.cse.odiga.global.jwt.JwtTokenProvider;
import yu.cse.odiga.global.type.Role;
import yu.cse.odiga.owner.dao.OwnerRepository;
import yu.cse.odiga.owner.domain.Owner;
import yu.cse.odiga.owner.dto.OwnerLoginDto;
import yu.cse.odiga.owner.dto.OwnerSignUpDto;

@Service
@RequiredArgsConstructor
@Transactional
public class OwnerAuthService {

    private final OwnerRepository ownerRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    public Owner ownerSignUp(OwnerSignUpDto ownerSignUpDto) {

        ownerRepository.findByEmail(ownerSignUpDto.getEmail()).ifPresent((user) -> {
            throw new BusinessLogicException("이미 존재하는 email 입니다.", HttpStatus.CONFLICT.value());
        });

        Owner owner = Owner.builder()
                .name(ownerSignUpDto.getName())
                .email(ownerSignUpDto.getEmail())
                .password(passwordEncoder.encode(ownerSignUpDto.getPassword()))
                .role(Role.ROLE_OWNER)
                .build();

        ownerRepository.save(owner);

        return owner;

    }

    public JwtTokenDto login(OwnerLoginDto ownerLoginDto) {

        Owner owner = ownerRepository.findByEmail(ownerLoginDto.getEmail())
                .orElseThrow(() -> new BusinessLogicException("존재하지 않는 계정 입니다.", HttpStatus.UNAUTHORIZED.value()));

        if (!passwordEncoder.matches(ownerLoginDto.getPassword(), owner.getPassword())) {
            throw new BusinessLogicException("비밀번호가 일치 하지 않습니다.", HttpStatus.UNAUTHORIZED.value());
        }

        return jwtTokenProvider.createToken(ownerLoginDto.getEmail());
    }
}
