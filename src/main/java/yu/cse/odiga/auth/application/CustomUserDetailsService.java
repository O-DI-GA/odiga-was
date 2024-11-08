package yu.cse.odiga.auth.application;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import lombok.RequiredArgsConstructor;
import yu.cse.odiga.auth.dao.UserRepository;
import yu.cse.odiga.auth.domain.CustomUserDetails;
import yu.cse.odiga.auth.domain.User;

@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

	private final UserRepository userRepository;

	@Override
	public UserDetails loadUserByUsername(String username) {
		User user = userRepository.findByEmail(username)
			.orElseThrow(() -> new UsernameNotFoundException("유저가 존재하지 않습니다."));
		return new CustomUserDetails(user);
	}
}