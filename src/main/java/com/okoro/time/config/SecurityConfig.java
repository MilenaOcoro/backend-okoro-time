package com.okoro.time.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.filter.CorsFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

   @Autowired
   private JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

   @Autowired
   private JwtRequestFilter jwtRequestFilter;

   @Autowired
   private CorsFilter corsFilter;

   @Bean
   public PasswordEncoder passwordEncoder() {
       return new BCryptPasswordEncoder();
   }

   @Bean
   public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
       return authConfig.getAuthenticationManager();
   }

   @Bean
   public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
       http 
           .cors(cors -> {})
           .csrf(csrf -> csrf.disable())
           .authorizeHttpRequests(auth -> auth
               .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
               .requestMatchers(HttpMethod.POST, "/api/auth/login").permitAll()
               .requestMatchers(HttpMethod.POST, "/api/users").permitAll()
               .requestMatchers(HttpMethod.POST, "/api/auth/create-admin").permitAll()  
               .requestMatchers(HttpMethod.GET, "/api/auth/verify").permitAll()
               .requestMatchers(HttpMethod.GET, "/", "/index.html", "/static/**", "/assets/**", "/css/**", "/js/**").permitAll()
               .anyRequest().authenticated()
           )
           .exceptionHandling(exception -> exception
               .authenticationEntryPoint(jwtAuthenticationEntryPoint)
           )
           .sessionManagement(session -> session
               .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
           );

       http.addFilterBefore(corsFilter, UsernamePasswordAuthenticationFilter.class);
       http.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);

       return http.build();
   }
}