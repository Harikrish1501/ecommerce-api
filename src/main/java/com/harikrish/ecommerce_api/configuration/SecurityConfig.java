package com.harikrish.ecommerce_api.configuration;
import com.harikrish.ecommerce_api.service.inf.IUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.CorsConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsPasswordService;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;


@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

    private final IUserService userService;
    private final JwtFilter jwtFilter;

    // This  method is used to configure the security filter chain which is used to filter the incoming requests and authenticate the user. We are disabling the CSRF protection and allowing all the requests to be authenticated. We are also setting the session management to stateless which means that the server will not create a session for the user and will not store any information about the user in the session. This is useful for REST APIs where we want to authenticate the user for each request.
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws  Exception{
        http.csrf(AbstractHttpConfigurer::disable);
        http.cors(cors->cors.configurationSource(request -> {
            CorsConfiguration configuration = new CorsConfiguration();
            configuration.addAllowedOrigin("http://localhost:4200");
            configuration.addAllowedHeader("*");
            configuration.addAllowedMethod("*");
            configuration.setMaxAge(3600L);
            return configuration;
                }));
        http.authorizeHttpRequests( request-> request
                .requestMatchers("/api/auth/register","/api/auth/login","/swagger-ui/**","/v3/api-docs/**").permitAll()
                .anyRequest().authenticated())
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);
        http.httpBasic(Customizer.withDefaults());
        http.sessionManagement(session-> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
         return http.build();
    }

    // This method is used to configure the authentication provider which is used to authenticate the user. We are using DaoAuthenticationProvider which is used to authenticate the user from the database. We are also setting the password encoder to NoOpPasswordEncoder which is used to encode the password in plain text. This is not recommended for production use, but it is used here for simplicity.
    @Bean
    public AuthenticationProvider authenticationProvider(){
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider(userService);
//        provider.setPasswordEncoder(NoOpPasswordEncoder.getInstance());
        provider.setPasswordEncoder(new BCryptPasswordEncoder(10));

        return provider;

    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration){
        return configuration.getAuthenticationManager();
    }

//    @Bean
//    public UserDetailsService userDetailsService(){
//        UserDetails user = User.withDefaultPasswordEncoder()
//                .username("Hari")
//                .password("12345")
//                .roles("USER")
//                .build();
//        UserDetails  admin = User.withDefaultPasswordEncoder()
//                .username("admin")
//                .password("1234")
//                .roles("ADMIN")
//                .build();
//        return new InMemoryUserDetailsManager(user,admin);
//    }


}
