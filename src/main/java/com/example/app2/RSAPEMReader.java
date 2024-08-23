package com.example.app2;

import org.bouncycastle.util.io.pem.PemObject;
import org.bouncycastle.util.io.pem.PemReader;

import java.io.FileReader;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

public class RSAPEMReader {
    private static PrivateKey readPrivateKeyFromPEM(String filePath) throws Exception {
        try (PemReader pemReader = new PemReader(new FileReader(filePath))) {
            PemObject pemObject = pemReader.readPemObject();
            byte[] pemContent = pemObject.getContent();
            PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(pemContent);
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            return keyFactory.generatePrivate(keySpec);
        }
    }

    private static PublicKey readPublicKeyFromPEM(String filePath) throws Exception {
        try (PemReader pemReader = new PemReader(new FileReader(filePath))) {
            PemObject pemObject = pemReader.readPemObject();
            byte[] pemContent = pemObject.getContent();
            X509EncodedKeySpec keySpec = new X509EncodedKeySpec(pemContent);
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            return keyFactory.generatePublic(keySpec);
        }
    }

    public static KeyPair getKeyPair(String privateKeyPath, String publicKeyPath) throws Exception {
        PrivateKey privateKey = readPrivateKeyFromPEM(privateKeyPath);
        PublicKey publicKey = readPublicKeyFromPEM(publicKeyPath);
        return new KeyPair(publicKey, privateKey);
    }
}
