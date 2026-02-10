package com.test.FundStack.security;

import jakarta.servlet.Filter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

/**
 * @author Sid
 * @author NaveenDhanasekaran
 * @author Vigneshwaran
 * 
 * History:
 * -08-01-2025 <Sid> SecurityConfiguration
 *      - InitialVersion
 * -12-02-2025 <NaveenDhanasekaran>
 *      - Altered SecurityFilterChain method
 * -21-02-2025 <NaveenDhanasekaran>
 *      - Added /api/trans path to allow
 * -24-02-2025 <NaveenDhanasekaran>
 *      - Add /api/swap path to allow security context.
 * -10-03-2025 <NaveenDhanasekaran>
 *      - Added exception handling in security
 * -24-04-2025 <NaveenDhanasekaran>
 *      - added uat controller in pulic urls
 * -29-04-2025 <NaveenDhanasekaran>
 *      - Added feed url to public 
 * -30-07-2025 <NaveenDhanasekaran>
 *      - Updated public urls to allow mfu callback urls
 * -26-09-2025 <NaveenDhanasekaran>
 *      - Removed authenticationProvider from SecurityFilterChain
 * -30-10-2025 <NaveenDhanasekaran>
 *      - added authV2 controller in public urls
 * -07-01-2026 <Sid>
 *     - added authV3 controller in public urls
 * -12-01-2026 <Sid>
 *     - Updated auth related urls to public urls
 * -20-01-2026 <Vigneshwaran>
 *     - Added an public url for device notification and check device exist
 *
 */

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfiguration {

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http
				.csrf(AbstractHttpConfigurer::disable)
				.authorizeHttpRequests(auth -> auth
						.requestMatchers("/**").permitAll()
						.anyRequest().authenticated()
				)
				.sessionManagement(session ->
						session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
				);

		return http.build();
	}


	private Filter corsFilter() {
		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		CorsConfiguration config = new CorsConfiguration();
		config.addAllowedOrigin("*");
		config.addAllowedHeader("*");
		config.addAllowedMethod("*");
		config.addExposedHeader("Permissions");
		config.addExposedHeader(HttpHeaders.AUTHORIZATION);
		source.registerCorsConfiguration("/**", config);
		return new CorsFilter(source);
	}

}
