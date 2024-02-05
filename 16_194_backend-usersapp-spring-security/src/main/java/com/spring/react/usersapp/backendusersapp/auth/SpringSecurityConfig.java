package com.spring.react.usersapp.backendusersapp.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import com.spring.react.usersapp.backendusersapp.auth.filters.JwtAuthenticationFilter;
import com.spring.react.usersapp.backendusersapp.auth.filters.JwtValidationFilter;

//194

@Configuration
public class SpringSecurityConfig {

    /*
    @Bean                                                                                               //se crea un componenete de spring en la forma de un método. o sea lo q devuelve este método se convierte en un componente.
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception{
        return http.authorizeHttpRequests().requestMatchers(HttpMethod.GET, "/users").permitAll()
                                            .anyRequest().authenticated()
                                            .and()
                                            .csrf(config -> config.disable())                           //esto es para formularios dentro de spring, se deshabilita ya q estamos usando react.
                                            .sessionManagement(management -> management.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                                            .build();
    }
    */

    @Bean 
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http.authorizeHttpRequests()
                .requestMatchers(HttpMethod.GET, "/users").permitAll()
                .requestMatchers(HttpMethod.GET, "/users/{id}").hasAnyRole("USER", "ADMIN")
                .requestMatchers(HttpMethod.POST, "/users").hasRole("ADMIN")
                .requestMatchers("/users/**").hasRole("ADMIN")
                //.requestMatchers(HttpMethod.DELETE, "/users/{id}").hasRole("ADMIN")
                //.requestMatchers(HttpMethod.PUT, "/users/{id}").hasRole("ADMIN")

                .anyRequest().authenticated()
                .and()
                .addFilter(new JwtAuthenticationFilter(authenticationConfiguration.getAuthenticationManager()))
                .addFilter(new JwtValidationFilter(authenticationConfiguration.getAuthenticationManager()))
                .csrf(config -> config.disable())
                .sessionManagement(managment -> managment.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .build();
    }


    @Bean
    PasswordEncoder passwordEncoder(){
        //return NoOpPasswordEncoder.getInstance();             //para pruebas con token en duro.
        return new BCryptPasswordEncoder();
    }

    @Bean
    AuthenticationManager authenticationManager () throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }


    @Autowired
    private AuthenticationConfiguration authenticationConfiguration;


}
