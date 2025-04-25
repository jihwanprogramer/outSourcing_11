package com.example.outsourcing_11.util;

import java.util.Date;

import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtUtil {
	private final String secretKey = "thisIsASecretKeyThatIsAtLeast32BytesLong!";  // 실제 서비스에서는 외부 환경변수로 관리

	// JWT 토큰 생성 (유저 id 포함)
	public String generateAccessToken(Long userId, String userName, String email, String role) {
		return Jwts.builder()
			.setSubject(String.valueOf(userId))  // 유저의 PK 값 (Long -> String으로 변환)
			.claim("userName", userName)
			.claim("email", email)
			.claim("role", role)
			.setIssuedAt(new Date())
			.setExpiration(new Date(System.currentTimeMillis() + 3000000)) // 1시간
			.signWith(Keys.hmacShaKeyFor(secretKey.getBytes()), SignatureAlgorithm.HS256)
			.compact();
	}

	// JWT에서 유저 id 값 추출
	public Long extractUserId(String token) {
		return Long.parseLong(Jwts.parserBuilder()
			.setSigningKey(secretKey.getBytes())
			.build()
			.parseClaimsJws(token)
			.getBody()
			.getSubject());  // subject에 유저 PK 값이 담겨있음
	}

	// JWT에서 유저 userName 값 추출
	public String extractUserNameFromToken(String token) {
		Claims claims = Jwts.parserBuilder()
			.setSigningKey(Keys.hmacShaKeyFor(secretKey.getBytes()))
			.build()
			.parseClaimsJws(token)
			.getBody();

		return claims.get("userName", String.class);
	}

	// JWT에서 유저 email 값 추출
	public String extractEmailFromToken(String token) {
		Claims claims = Jwts.parserBuilder()
			.setSigningKey(Keys.hmacShaKeyFor(secretKey.getBytes()))
			.build()
			.parseClaimsJws(token)
			.getBody();

		return claims.get("email", String.class);
	}

	// JWT에서 유저 role 값 추출
	public String extractRoleFromToken(String token) {
		Claims claims = Jwts.parserBuilder()
			.setSigningKey(Keys.hmacShaKeyFor(secretKey.getBytes()))
			.build()
			.parseClaimsJws(token)
			.getBody();

		return claims.get("role", String.class);
	}

	// 토큰 유효성 체크
	public boolean validateToken(String token) {
		try {
			Jwts.parserBuilder().setSigningKey(secretKey.getBytes()).build().parseClaimsJws(token);
			return true;
		} catch (Exception e) {
			return false;
		}
	}
}
