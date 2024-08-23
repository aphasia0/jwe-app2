package com.example.app2;


import io.jsonwebtoken.Jwts;
import jakarta.servlet.http.Cookie;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;

import java.security.KeyPair;

@Controller
public class HomeController {
    @GetMapping("/")
    @CrossOrigin(origins = "*")
    public String home(Model model,@CookieValue("app-context") String jwe) {
        model.addAttribute("message", "Welcome to our homepage APP2!");
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


        model.addAttribute("jwe",sub);
        return "home";
    }
}
