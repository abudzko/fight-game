package budzko.backend.starters.jwt.filter;

import budzko.backend.starters.jwt.service.JwtValidationService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@RequiredArgsConstructor
@Slf4j
@Component
public class JwtTokenFilter extends OncePerRequestFilter {
    public static final String BEARER_PREFIX = "Bearer ";
    private final JwtValidationService jwtValidationService;

    private static String extractAuthHeader(HttpServletRequest request) {
//        final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
//        if (hasLength(authHeader) || !authHeader.startsWith(BEARER_PREFIX)) {
//            throw new AuthorizationServiceException("Access token is expected");
//        }
//        return authHeader;
        return BEARER_PREFIX + "JWT";
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {
        final String authHeader = extractAuthHeader(request);
        final String jwt = authHeader.substring(authHeader.indexOf(" ") + 1);
        UserDetails userDetails = jwtValidationService.validateJwt(jwt);
        UsernamePasswordAuthenticationToken
                authentication = new UsernamePasswordAuthenticationToken(
                userDetails,
                null,
                userDetails == null ? List.of() : userDetails.getAuthorities()
        );
        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        filterChain.doFilter(request, response);
    }
}
