package com.enigma.purba_resto.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.enigma.purba_resto.entity.AppUser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

@Component
@Slf4j
public class JwtUtil {
//    private final Logger log = Logger.getLogger(this.getClass().getName());// ini bisa digantikan dgn @Slf4j

    // ini tidak tepat jia disimpan disini,, seharusnya di tempat yang lebih aman misal applivation.properties
    @Value("${app.PURBA-RESTO.jwt-secret}")
    private String jwtSecret;
    @Value("${app.PURBA-RESTO.app-name}")
    private String appName; // sebenarnya ini sangat rahasia.. bahaya jika orang lain tahu.
    @Value("${app.PURBA-RESTO.jwtExpirationInSeconds}")
    private long jwtExpirationInSeconds;

    // fungsi ini untuk meng-generate TOKEN
    public String generateToken(AppUser appUser) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(jwtSecret.getBytes(StandardCharsets.UTF_8));
            /*// testing validitas waktu
            Instant now = Instant.now();
            Instant expiration = now.plusSeconds(20);

            // Tambahkan logging untuk melihat nilai timestamp
            log.info("Waktu sekarang (iat): {}", now);
            log.info("Waktu kedaluwarsa (exp): {}", expiration);*/
            String token = JWT.create()
                    .withIssuer(appName) // issuer itu, yang/siapa nyetak token
                    .//withExpiresAt(Date.from(Instant.now().plus(7, ChronoUnit.DAYS))) // Token berlaku 7 hari
                     withSubject(appUser.getId())
                    .withExpiresAt(Instant.now().plusSeconds(jwtExpirationInSeconds))//berlaku 60*5 detik, jika pakai detik
                    .withIssuedAt(Instant.now())// kapan mulai nyetak token? ya sekarang
                    .withClaim("role",appUser.getRole().name())
                    .sign(algorithm);
            return token;
        } catch (JWTCreationException exception){
            log.error("error while generating token Suuu : {}", exception.getMessage());
            // Invalid Signing configuration / Couldn't convert Claims.
            throw new RuntimeException(exception);
        }
    }
    // funsi ini untuk validasi, apakah token valid atau tidak
    public boolean verifyJwtToken(String token) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(jwtSecret.getBytes(StandardCharsets.UTF_8));
            JWTVerifier verifier = JWT.require(algorithm).build();
            DecodedJWT decodedJWT = verifier.verify(token);
            return decodedJWT.getIssuer().equals(appName);
        }catch (JWTVerificationException exception){
            log.error("invalid while verifiing token Suuu : {}", exception.getMessage());
            return false;
        }
    }
     public Map<String, String> getUserInfoByToken(String token) {
         try {
             Algorithm algorithm = Algorithm.HMAC256(jwtSecret.getBytes(StandardCharsets.UTF_8));
             JWTVerifier verifier = JWT.require(algorithm).build();
             DecodedJWT decodedJWT = verifier.verify(token);

             Map<String, String> userInfo = new HashMap<>();
             userInfo.put("userId", decodedJWT.getSubject());
             userInfo.put("role", decodedJWT.getClaim("role").asString());
             return userInfo;

         }catch (JWTVerificationException exception){
             log.error("invalid while verifiing token Suuu : {}", exception.getMessage());
             return null;
         }
     }
}
