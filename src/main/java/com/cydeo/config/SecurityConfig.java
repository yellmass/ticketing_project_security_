package com.cydeo.config;

import com.cydeo.service.SecurityService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
public class SecurityConfig {
    private final SecurityService securityService;
    private final AuthSuccessHandler authSuccessHandler;

    public SecurityConfig(SecurityService securityService, AuthSuccessHandler authSuccessHandler) {
        this.securityService = securityService;
        this.authSuccessHandler = authSuccessHandler;
    }

//    @Bean
//    public UserDetailsService userDetailsService(PasswordEncoder passwordEncoder){
//        List<UserDetails> userList = new ArrayList<>();
//        userList.add(
//          new User("mike@gmail.com", passwordEncoder.encode("abc123"), Arrays.asList(new SimpleGrantedAuthority("ROLE_ADMIN")))
//        );
//        userList.add(
//                new User("yilmaz@gmail.com", passwordEncoder.encode("xyz123"), Arrays.asList(new SimpleGrantedAuthority("ROLE_MANAGER")))
//        );
//
//        return new InMemoryUserDetailsManager(userList);
//    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
                .authorizeRequests()
                .requestMatchers("/user/**").hasAuthority("Admin")
                .requestMatchers("/project/**").hasAuthority("Manager")
                .requestMatchers("/task/employee/**").hasAuthority("Employee")
                .requestMatchers("/task/**").hasAuthority("Manager")
//                .requestMatchers("/user/**").hasRole("ADMIN")  // -> to use hasRole, role column should be ROLE_ADMIN in database.. hasRole adds ROLE_ to the string provided
//                .requestMatchers("/task/employee/**").hasAnyRole("MANAGER","EMPLOYEE")
                .requestMatchers(
                        "/",
                        "/login",
                        "/assets/**",
                        "/images/**",
                        "/fragments/**"
                ).permitAll()
                .anyRequest().authenticated()
                .and()
                //.httpBasic(Customizer.withDefaults())
                .formLogin(formLogin -> formLogin
                        .loginPage("/login")
                        //.defaultSuccessUrl("/welcome")
                        .successHandler(authSuccessHandler)
                        .failureUrl("/login?error=true")
                        .permitAll()
                )
                .logout(logout->logout
                        .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                        .logoutSuccessUrl("/login")
                )
                .rememberMe(rememberMe->rememberMe
                        .tokenValiditySeconds(120)
                        .key("cydeo")
                        .userDetailsService(securityService)
                )
                .build();

    }


}
