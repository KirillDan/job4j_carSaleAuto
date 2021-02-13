package ru.job4j.auth;

import javax.crypto.SecretKey;

import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

public interface Key {
	static SecretKey secretKey = Keys.secretKeyFor(SignatureAlgorithm.HS512);
}
