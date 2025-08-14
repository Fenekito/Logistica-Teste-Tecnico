package com.krill.logistica.controller;

import com.krill.logistica.dto.LoginRequestDto;
import com.krill.logistica.dto.LoginResponseDto;
import com.krill.logistica.service.UserService;
import com.krill.logistica.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.Collection;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UserService userService;

    @PostMapping("/login")
    @ResponseStatus(HttpStatus.OK)
    public LoginResponseDto login(@RequestBody @Valid LoginRequestDto req) {

        Authentication auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(req.email(), req.password())
        );

        var userDetails = (org.springframework.security.core.userdetails.UserDetails) auth.getPrincipal();
        Collection<? extends GrantedAuthority> authorities = userDetails.getAuthorities();

        String token = jwtService.generateToken(userDetails.getUsername(), authorities);

        return new LoginResponseDto(token, "Bearer " + token);
    }
}
