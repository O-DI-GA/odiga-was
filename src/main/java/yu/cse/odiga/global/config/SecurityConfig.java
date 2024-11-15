package yu.cse.odiga.global.config;

import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.annotation.web.configurers.HttpBasicConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfigurationSource;

import yu.cse.odiga.auth.application.CustomUserDetailsService;
import yu.cse.odiga.auth.dao.UserRepository;
import yu.cse.odiga.global.handler.UserAuthenticationEntryPoint;
import yu.cse.odiga.global.jwt.JwtAuthenticationFilter;
import yu.cse.odiga.global.jwt.JwtExceptionHandlingFilter;
import yu.cse.odiga.global.jwt.JwtTokenProvider;
import yu.cse.odiga.global.jwt.UserAccessDeniedHandler;
import yu.cse.odiga.owner.application.OwnerUserDetailsService;
import yu.cse.odiga.owner.dao.OwnerRepository;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

	private final CorsConfigurationSource corsConfigurationSource;
	private final JwtExceptionHandlingFilter jwtExceptionHandlingFilter;

	@Bean
	public SecurityFilterChain userFilterChain(HttpSecurity http,
		JwtTokenProvider jwtTokenProvider,
		@Qualifier("userAuthenticationProvider") AuthenticationProvider authenticationProvider,
		CustomUserDetailsService customUserDetailsService) throws Exception {
		http
			.securityMatcher("/api/v1/user/**")

			.authorizeHttpRequests(auth -> auth
				.requestMatchers("/api/v1/user/auth/**").permitAll()
				.requestMatchers(PathRequest.toH2Console()).permitAll() // 이 설정을 따로 빼줘야할듯
				.anyRequest().authenticated()
			)
			// h2 설정
			.headers(header -> header.frameOptions(HeadersConfigurer.FrameOptionsConfig::sameOrigin))

			.httpBasic(HttpBasicConfigurer::disable) // Rest 방식 -> 원래는 web 에서 username, password 를 받는다.
			.csrf(CsrfConfigurer::disable) // Rest 방식 -> rest 방식은 csrf 공격을 받을리가 없다.

			.cors((cors) -> cors.configurationSource(corsConfigurationSource))

			.sessionManagement(sessionManagement ->
				sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS) // session 을 사용하지 않음
			)

			.exceptionHandling(ex -> ex.authenticationEntryPoint(new UserAuthenticationEntryPoint())
				.accessDeniedHandler(new UserAccessDeniedHandler()))

			.authenticationProvider(authenticationProvider)

			.addFilterBefore(new JwtAuthenticationFilter(jwtTokenProvider, customUserDetailsService),
				UsernamePasswordAuthenticationFilter.class) // JwtAuthenticationFilter 작동 후 UsernamePasswordAuthenticationFilter 변경

			.addFilterBefore(jwtExceptionHandlingFilter, JwtAuthenticationFilter.class);

		return http.build();
	}

	@Bean
	public SecurityFilterChain ownerFilterChain(HttpSecurity http,
		JwtTokenProvider jwtTokenProvider,
		@Qualifier("ownerAuthenticationProvider") AuthenticationProvider authenticationProvider,
		OwnerUserDetailsService ownerUserDetailsService) throws Exception {
		http
			.securityMatcher("/api/v1/owner/**")

			.authorizeHttpRequests(auth -> auth
				.requestMatchers("/api/v1/owner/auth/**").permitAll()
				.anyRequest().authenticated()
			)

			.headers(header -> header.frameOptions(HeadersConfigurer.FrameOptionsConfig::sameOrigin))

			.httpBasic(HttpBasicConfigurer::disable)

			.csrf(CsrfConfigurer::disable) // Rest 방식 -> rest 방식은 csrf 공격을 받을리가 없다.

			.cors((cors) -> cors.configurationSource(corsConfigurationSource))

			.sessionManagement(sessionManagement ->
				sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS) // session 을 사용하지 않음
			)

			.exceptionHandling(ex -> ex.authenticationEntryPoint(new UserAuthenticationEntryPoint())
				.accessDeniedHandler(new UserAccessDeniedHandler()))

			.authenticationProvider(authenticationProvider)

			.addFilterBefore(new JwtAuthenticationFilter(jwtTokenProvider, ownerUserDetailsService),
				UsernamePasswordAuthenticationFilter.class)

			.addFilterBefore(jwtExceptionHandlingFilter, JwtAuthenticationFilter.class);

		return http.build();
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public CustomUserDetailsService customUserDetailsService(UserRepository userRepository) {
		return new CustomUserDetailsService(userRepository);
	}

	@Bean
	@Qualifier("userAuthenticationProvider")
	public AuthenticationProvider userAuthenticationProvider(UserRepository userRepository,
		PasswordEncoder passwordEncoder) {
		DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
		provider.setUserDetailsService(customUserDetailsService(userRepository));
		provider.setPasswordEncoder(passwordEncoder);
		return provider;
	}

	@Bean
	public OwnerUserDetailsService ownerUserDetailsService(OwnerRepository ownerRepository) {
		return new OwnerUserDetailsService(ownerRepository);
	}

	@Bean
	@Qualifier("ownerAuthenticationProvider")
	public AuthenticationProvider ownerAuthenticationProvider(OwnerRepository ownerRepository,
		PasswordEncoder passwordEncoder) {
		DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
		provider.setUserDetailsService(ownerUserDetailsService(ownerRepository));
		provider.setPasswordEncoder(passwordEncoder);
		return provider;
	}
}