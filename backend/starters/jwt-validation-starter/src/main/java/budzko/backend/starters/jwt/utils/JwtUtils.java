package budzko.backend.starters.jwt.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;

import java.security.Key;

public class JwtUtils {
    public static Jws<Claims> parse(String jwt, Key publicKey) {
        return Jwts.parserBuilder().setSigningKey(publicKey).build().parseClaimsJws(jwt);
    }
}
