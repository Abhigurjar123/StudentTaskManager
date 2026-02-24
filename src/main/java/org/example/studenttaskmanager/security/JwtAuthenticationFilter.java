package org.example.studenttaskmanager.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final CustomUserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        // 1️⃣ Authorization header read karo
        String authHeader = request.getHeader("Authorization");

        // 2️⃣ Agar header missing ya invalid format me hai, aage pass kar do
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        // 3️⃣ Token extract karo
        String token = authHeader.substring(7);

        // 4️⃣ Token se username nikalo
        String username = jwtService.extractUsername(token);

        // 5️⃣ Agar username mila aur user already authenticated nahi hai
        if (username != null &&
                SecurityContextHolder.getContext().getAuthentication() == null) {

            // 6️⃣ DB se user load karo
            UserDetails userDetails =
                    userDetailsService.loadUserByUsername(username);

            // 7️⃣ Token validate karo
            if (jwtService.isTokenValid(token, userDetails)) {

                // 8️⃣ Authentication object create karo
                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(
                                userDetails,
                                null,
                                userDetails.getAuthorities()
                        );

                authentication.setDetails(
                        new WebAuthenticationDetailsSource()
                                .buildDetails(request)
                );

                // 9️⃣ SecurityContext me set karo
                SecurityContextHolder.getContext()
                        .setAuthentication(authentication);
            }
        }

        // 🔟 Request ko next filter me bhejo
        filterChain.doFilter(request, response);
    }
}