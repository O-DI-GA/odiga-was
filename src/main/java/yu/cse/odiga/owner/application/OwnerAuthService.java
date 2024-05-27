package yu.cse.odiga.owner.application;


import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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
        return jwtTokenProvider.createToken(ownerLoginDto.getEmail());
    }
}
