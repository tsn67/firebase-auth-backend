package com.dependancy.firebaseauthsystem.filter;

import com.dependancy.firebaseauthsystem.entities.User;
import com.dependancy.firebaseauthsystem.repositories.UserRepository;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseToken;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;

@Component
@AllArgsConstructor
public class FirebaseTokenFilter extends OncePerRequestFilter {


    private final UserRepository userRepository;


    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        String HEADER = "Authorization";
        String PREFIX = "Bearer ";
        String authHeader = request.getHeader(HEADER);

        if (authHeader != null && authHeader.startsWith(PREFIX)) {
            String token = authHeader.replace(PREFIX, "");
            try {
                FirebaseToken firebaseToken = FirebaseAuth.getInstance().verifyIdToken(token);

                String email = firebaseToken.getEmail();


                User user = userRepository.findByEmail(email).orElse(null);

                if (user == null) {
                    user = new User();
                    user.setEmail(email);
                    userRepository.save(user);
                }

                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(user.getEmail(), null, Collections.emptyList());

                SecurityContextHolder.getContext().setAuthentication(authentication);
            } catch (Exception e) {
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid Firebase ID token");
                return;
            }
        }

        filterChain.doFilter(request, response);
    }
}
