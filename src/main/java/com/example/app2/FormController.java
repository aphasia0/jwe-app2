package com.example.app2;

import io.jsonwebtoken.Jwts;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.view.RedirectView;

import java.security.KeyPair;

@Controller
public class FormController {

    @PostMapping("my-form")
    public RedirectView myForm(@ModelAttribute Greeting greeting, Model model, HttpServletResponse response) {
        System.out.println(greeting.jwe);
        KeyPair pair = null;
        try {
            pair = RSAPEMReader.getKeyPair("private_key.pem", "public_key.pem");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        String sub = Jwts.parser()
                .decryptWith(pair.getPrivate()) // <-- Alice's RSA private key
                .build().parseEncryptedClaims(greeting.jwe).getPayload().getSubject();
        System.out.println("Decrypt ok: SUB -->" + sub);
        Cookie cookie = new Cookie("app-context", greeting.jwe);
        cookie.setMaxAge(7 * 24 * 60 * 60);
        cookie.setPath("/");
        cookie.setSecure(true);
        cookie.setHttpOnly(true);
        response.addCookie(cookie);
        return new RedirectView("http://localhost:8081/");
    }
}
