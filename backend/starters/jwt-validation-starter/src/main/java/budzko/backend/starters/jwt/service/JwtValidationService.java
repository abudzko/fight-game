package budzko.backend.starters.jwt.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.List;

@Service
@Slf4j
public class JwtValidationService {
    public UserDetails validateJwt(String jwt) {
//        Jws<Claims> claimsJws = JwtUtils.parse(jwt, getPublicKey());
//        Object username = claimsJws.getBody().get("username");
        return User.builder()
                .username("user")
                .password("pass")
                .authorities(List.of(new SimpleGrantedAuthority("PLAYER")))
                .build();
    }

    private Key getPublicKey() {
        return null;
    }
}
