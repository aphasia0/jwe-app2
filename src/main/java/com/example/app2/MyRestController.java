package com.example.app2;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.AeadAlgorithm;
import io.jsonwebtoken.security.KeyAlgorithm;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;

@RestController
public class MyRestController {
    @CrossOrigin(origins = "http://localhost:4200", allowCredentials = "true")

    @PostMapping("/app2-api")
    //get data from post request
    public Msg getPostData(@RequestBody String jwe, HttpServletResponse response) {
        System.out.println(jwe);
        KeyPair pair = null;
        try {
            pair = RSAPEMReader.getKeyPair("private_key.pem", "public_key.pem");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
         String sub = Jwts.parser()
                .decryptWith(pair.getPrivate()) // <-- Alice's RSA private key
                .build().parseEncryptedClaims(jwe).getPayload().getSubject();

        Cookie cookie = new Cookie("myCookie2", jwe);
        cookie.setMaxAge(7 * 24 * 60 * 60);
        cookie.setPath("/");
        response.addCookie(cookie);
        return new Msg("http://localhost:8081");
    }


}
