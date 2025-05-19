package com.okoro.time.config;

import com.okoro.time.security.JwtUtils; 
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtRequestFilter extends OncePerRequestFilter {
   private static final Logger logger = LoggerFactory.getLogger(JwtRequestFilter.class);
   
   @Autowired
   private UserDetailsService userDetailsService;
   
   @Autowired
   private JwtUtils jwtUtils;

   @Override
   protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
           throws ServletException, IOException {

       String path = request.getRequestURI();
       String method = request.getMethod();

       if (method.equals("OPTIONS")) {
           response.setStatus(HttpServletResponse.SC_OK);
           chain.doFilter(request, response);
           return;
       }

       if (path.contains("/api/auth")) {
           chain.doFilter(request, response);
           return;
       }
       
       final String requestTokenHeader = request.getHeader("Authorization");
       
       String username = null;
       String jwtToken = null;
       
       if (requestTokenHeader != null && requestTokenHeader.startsWith("Bearer ")) {
           jwtToken = requestTokenHeader.substring(7);
           try {
               username = jwtUtils.getUsernameFromJwtToken(jwtToken);
           } catch (IllegalArgumentException e) {
               logger.error("Unable to get JWT token");
           } catch (ExpiredJwtException e) {
               logger.error("JWT token expired");
           }
       } else {
           logger.warn("JWT Token does not begin with Bearer String");
       }
       
       if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
           UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);
           
           if (jwtUtils.validateJwtToken(jwtToken)) {
               UsernamePasswordAuthenticationToken authenticationToken = 
                       new UsernamePasswordAuthenticationToken(
                               userDetails, null, userDetails.getAuthorities());
               
               authenticationToken.setDetails(
                       new WebAuthenticationDetailsSource().buildDetails(request));
               
               SecurityContextHolder.getContext().setAuthentication(authenticationToken);
           }
       }
       
       chain.doFilter(request, response);
   }
}