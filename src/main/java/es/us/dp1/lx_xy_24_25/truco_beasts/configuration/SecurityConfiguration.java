package es.us.dp1.lx_xy_24_25.truco_beasts.configuration;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import static org.springframework.security.config.Customizer.withDefaults;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import es.us.dp1.lx_xy_24_25.truco_beasts.configuration.jwt.AuthEntryPointJwt;
import es.us.dp1.lx_xy_24_25.truco_beasts.configuration.jwt.AuthTokenFilter;
import es.us.dp1.lx_xy_24_25.truco_beasts.configuration.services.UserDetailsServiceImpl;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

	@Autowired
	UserDetailsServiceImpl userDetailsService;

	@Autowired
	private AuthEntryPointJwt unauthorizedHandler;

	@Autowired
	DataSource dataSource;

	private static final String ADMIN = "ADMIN";
	private static final String PLAYER = "PLAYER";
	

	@Bean
	protected SecurityFilterChain configure(HttpSecurity http) throws Exception {
		
		http
			.cors(withDefaults())		
			.csrf(AbstractHttpConfigurer::disable)		
			.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))			
			.headers((headers) -> headers.frameOptions((frameOptions) -> frameOptions.disable()))
			.exceptionHandling((exepciontHandling) -> exepciontHandling.authenticationEntryPoint(unauthorizedHandler))			
			
			.authorizeHttpRequests(authorizeRequests ->	authorizeRequests
			.requestMatchers("/api/v1/developers").permitAll()
			.requestMatchers(AntPathRequestMatcher.antMatcher("/resources/**")).permitAll()
			.requestMatchers(AntPathRequestMatcher.antMatcher("/webjars/**")).permitAll() 
			.requestMatchers(AntPathRequestMatcher.antMatcher("/static/**")).permitAll() 
			.requestMatchers(AntPathRequestMatcher.antMatcher("/resources/images/trofeos/**")).permitAll()
			.requestMatchers(AntPathRequestMatcher.antMatcher("/swagger-resources/**")).permitAll()						
			.requestMatchers(AntPathRequestMatcher.antMatcher("/")).permitAll()
			.requestMatchers(AntPathRequestMatcher.antMatcher("/oups")).permitAll()
			.requestMatchers(AntPathRequestMatcher.antMatcher("/api/v1/auth/**")).permitAll()
			.requestMatchers(AntPathRequestMatcher.antMatcher("/v3/api-docs/**")).permitAll()
			.requestMatchers(AntPathRequestMatcher.antMatcher("/swagger-ui.html")).permitAll()
			.requestMatchers(AntPathRequestMatcher.antMatcher("/swagger-ui/**")).permitAll()												
			.requestMatchers(AntPathRequestMatcher.antMatcher("/api/v1/developers")).permitAll()												
			.requestMatchers(AntPathRequestMatcher.antMatcher("/api/v1/plan")).permitAll()
			.requestMatchers(AntPathRequestMatcher.antMatcher("/api/v1/users/**")).hasAuthority(ADMIN)
			.requestMatchers(AntPathRequestMatcher.antMatcher("/users/**")).hasAuthority(ADMIN)
			.requestMatchers(AntPathRequestMatcher.antMatcher("/admin/**")).hasAuthority(ADMIN)
			.requestMatchers(AntPathRequestMatcher.antMatcher("/h2-console/**")).permitAll()
			.requestMatchers(AntPathRequestMatcher.antMatcher("/api/v1/partida/**")).authenticated()
			.requestMatchers(AntPathRequestMatcher.antMatcher("/api/v1/jugador/**")).authenticated() 
			.requestMatchers(AntPathRequestMatcher.antMatcher("/api/v1/partidajugador/**")).authenticated() 
			.requestMatchers("/api/v1/profile/edit").authenticated()
			.requestMatchers("/ws").permitAll()
			.requestMatchers("/app/mensaje").permitAll()
			.requestMatchers("/api/v1/chat/**").permitAll()
			.requestMatchers(AntPathRequestMatcher.antMatcher(HttpMethod.DELETE, "/api/v1/profile/borrarMiCuenta")).authenticated()
			.anyRequest().authenticated())					
			
			.addFilterBefore(authenticationJwtTokenFilter(), UsernamePasswordAuthenticationFilter.class);		
		return http.build();
	}

	@Bean
	public AuthTokenFilter authenticationJwtTokenFilter() {
		return new AuthTokenFilter();
	}

	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception{
		return config.getAuthenticationManager();
	}	


	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	
	
}
