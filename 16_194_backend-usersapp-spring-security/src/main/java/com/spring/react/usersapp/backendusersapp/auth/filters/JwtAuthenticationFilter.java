package com.spring.react.usersapp.backendusersapp.auth.filters;

import java.io.IOException;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.spring.react.usersapp.backendusersapp.auth.TokenJwtConfig;
import com.spring.react.usersapp.backendusersapp.model.entity.User;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private AuthenticationManager authenticationManager;

    public JwtAuthenticationFilter (AuthenticationManager authenticationManager){
        this.authenticationManager = authenticationManager;
    }


    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        
        User user = null;
        String username = null;
        String password = null;

        try {
            user = new ObjectMapper().readValue(request.getInputStream(), User.class);
            username = user.getUsername();
            password = user.getPassword();
            logger.info("Username desde request inputStream (raw) " + username);     //el logger es solo para desarrollo                            //raw : se refiere al body de request.
            logger.info("Password desde request inputStream (raw) " + password);        //el logger es solo para desarrollo    
        } catch (IOException e) {
            e.printStackTrace();
        }
        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(username, password);
        return this.authenticationManager.authenticate(authToken);
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
       
        String username = ((org.springframework.security.core.userdetails.User) authResult.getPrincipal()).getUsername();
       
        Collection <? extends GrantedAuthority> roles = authResult.getAuthorities();

        boolean isAdmin = roles.stream().anyMatch(r -> r.getAuthority().equals("ROLE_ADMIN"));

        Claims claims = Jwts.claims();
        
        claims.put("authorities", new ObjectMapper().writeValueAsString(roles));            //se agregan los roles pero como json.

        claims.put("isAdmin", isAdmin);

       //Construye el token
       String token = Jwts.builder()
                                    .setClaims(claims)
                                    .setSubject(username)
                                    .signWith(TokenJwtConfig.SECRET_KEY)
                                    .setIssuedAt(new Date())
                                    .setExpiration(new Date(System.currentTimeMillis() + 3_600_000))
                                    .compact();
       
       response.addHeader(TokenJwtConfig.HEADER_AUTHORIZATION, TokenJwtConfig.PREFIX_TOKEN + token);

       Map<String, Object> body = new HashMap<>();
       body.put("token", token);
       body.put("message", String.format("Hola %s, has iniciado sesión con éxito!", username));
       body.put("username", username);
       response.getWriter().write(new ObjectMapper().writeValueAsString(body));                                                 //consvierte el map en un jason y se manda en el cuerpo de la respuesta.
       response.setStatus(200);
       response.setContentType("application/json");
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {
        Map<String, Object> body = new HashMap<>();
        body.put("message", "Error de autenticación!");
        body.put("error", failed.getMessage());
        response.getWriter().write(new ObjectMapper().writeValueAsString(body));
        response.setStatus(401);
        response.setContentType("application/json");
    }

    

}
