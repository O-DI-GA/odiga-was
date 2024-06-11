package yu.cse.odiga.owner.application;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import yu.cse.odiga.owner.dao.OwnerRepository;
import yu.cse.odiga.owner.domain.Owner;
import yu.cse.odiga.owner.domain.OwnerUserDetails;

@RequiredArgsConstructor
public class OwnerUserDetailsService implements UserDetailsService {

    private final OwnerRepository ownerRepository;

    @Override
    public UserDetails loadUserByUsername(String username) {
        System.out.println("owner service");
        Owner owner = ownerRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("user not found"));
        return new OwnerUserDetails(owner);
    }
}
