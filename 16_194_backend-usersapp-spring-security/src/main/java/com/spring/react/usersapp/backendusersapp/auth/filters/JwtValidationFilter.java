package com.spring.react.usersapp.backendusersapp.auth.filters;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.spring.react.usersapp.backendusersapp.auth.TokenJwtConfig;

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

        //Se hace la comprobación del token, para esto hay que decodificarlo
        byte [] tokenDecodeBytes = Base64.getDecoder().decode(token);
        String tokenDecode = new String(tokenDecodeBytes);
        String [] tokenArray = tokenDecode.split("\\.");                                                                                  //se hace split con un punto. se pone \\ para escapar el punto, ya q es un caracter reservado.
        String secret = tokenArray[0];
        String username = tokenArray[1];

        //Esto corresponde al 1er elemento del arreglo. Si son iguales, autenticamos y dejamos pasar.
        if(TokenJwtConfig.SECRET_KEY.equals(secret)){
            List<GrantedAuthority>authorities = new ArrayList<>();
            authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username, null, authorities); //el parametro de credentials, que es el segundo parámetro, se pasa null pq este es importante para generar el token, pero no para validarlo, como se está haciendo en este filtro.
            SecurityContextHolder.getContext().setAuthentication(authenticationToken);                                                   //con esto se autentica y se deja pasar a usuario al recurso protegido por el token.
            chain.doFilter(request, response);                                                                                           //se continua con la cadena y se pasa el request y el response.
        } else {    
            Map<String, String> body = new HashMap<>();
            body.put("message", "El token no es válido!");
            response.getWriter().write(new ObjectMapper().writeValueAsString(body));
            response.setStatus(403);
            response.setContentType("application/json");
        }
    }

    

}