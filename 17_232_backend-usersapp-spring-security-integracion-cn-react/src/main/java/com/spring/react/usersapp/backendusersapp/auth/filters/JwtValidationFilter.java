package com.spring.react.usersapp.backendusersapp.auth.filters;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.spring.react.usersapp.backendusersapp.auth.SimpleGrantedAuthorityJsonCreator;
import com.spring.react.usersapp.backendusersapp.auth.TokenJwtConfig;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class JwtValidationFilter extends BasicAuthenticationFilter {

    public JwtValidationFilter(AuthenticationManager authenticationManager) {
        super(authenticationManager);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {

        String header = request.getHeader(TokenJwtConfig.HEADER_AUTHORIZATION);

        //EL token no siempre va  a venir ya que algunas rutas van a ser públicas, por lo tanto hay que validar:

        if(header == null || !header.startsWith(TokenJwtConfig.PREFIX_TOKEN)) {
            //Se continua con la cadena de los filtros y se pasa el request y el response
            chain.doFilter(request, response);
            return;
        }

        String token = header.replace(TokenJwtConfig.PREFIX_TOKEN, "");

        //Esto corresponde al 1er elemento del arreglo. Si son iguales, autenticamos y dejamos pasar.
        try {

            Claims claims = Jwts.parserBuilder().setSigningKey(TokenJwtConfig.SECRET_KEY).build().parseClaimsJws(token).getBody();  //se valida que el token de la cabecera, tenga la misma firma.
            
            Object authoritiesClaims = claims.get("authorities");

            String username = claims.getSubject(); //forma 1
            
            System.out.println("°°° Username: " + username);
            //222 - 223
            Collection< ? extends GrantedAuthority>authorities = Arrays.asList(new ObjectMapper()
                                                                .addMixIn(SimpleGrantedAuthority.class, SimpleGrantedAuthorityJsonCreator.class)
                                                                .readValue(authoritiesClaims.toString()
                                                                .getBytes(), SimpleGrantedAuthority[].class));                              // ObjectMapper : para pasar de json a obj.
    
            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username, null, authorities); //el parametro de credentials, que es el segundo parámetro, se pasa null pq este es importante para generar el token, pero no para validarlo, como se está haciendo en este filtro.
            
            SecurityContextHolder.getContext().setAuthentication(authenticationToken);                                                      //con esto se autentica y se deja pasar a usuario al recurso protegido por el token.
            
            chain.doFilter(request, response);                                                                                                //se continua con la cadena y se pasa el request y el response.
        
        } catch(JwtException e) {    
            
            Map<String, String> body = new HashMap<>();
            
            body.put("error", e.getMessage());
            body.put("message", "El token no es válido!");
            
            response.getWriter().write(new ObjectMapper().writeValueAsString(body));
            response.setStatus(401);
            response.setContentType("application/json");
        }
    }

    

}
