package itm.proyectoharoldo.backend.Utility;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtUtil {

    private static final String SECRET = "IchSprecheNichtDeutschEsTutMirLeid12345";

    private final Algorithm algorithm = Algorithm.HMAC256(SECRET);

    public String generateToken(String username) {
        return JWT.create()
                .withSubject(username)
                .withIssuedAt(new Date())
                .withExpiresAt(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10)) // 10 horas
                .sign(algorithm);
    }

    public String extractUsername(String token) {
        return JWT.require(algorithm)
                .build()
                .verify(token)
                .getSubject();
    }

    public boolean validateToken(String token, UserDetails userDetails) {
        try {
            String username = extractUsername(token);
            return username.equals(userDetails.getUsername());
        } catch (JWTVerificationException e) {
            return false;
        }
    }
}
