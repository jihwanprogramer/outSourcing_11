package com.example.outsourcing_11.config.security;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.io.PrintWriter;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.AuthenticationException;

@ExtendWith(MockitoExtension.class)
class JwtAuthenticationEntryPointTest {

	@InjectMocks
	private JwtAuthenticationEntryPoint entryPoint;

	@Mock
	private HttpServletRequest request;

	@Mock
	private HttpServletResponse response;

	@Mock
	private PrintWriter writer;

	@Mock
	private AuthenticationException authException;

	@Test
	void 인증실패_401반환_및_메시지출력() throws IOException {
		// given
		when(response.getWriter()).thenReturn(writer);

		// when
		entryPoint.commence(request, response, authException);

		// then
		verify(response).setStatus(HttpServletResponse.SC_UNAUTHORIZED); // 401
		verify(response).setContentType("application/json; charset=UTF-8");
		verify(writer).write("{ \"message\": \"로그인이 필요합니다.\" }");
	}

	@Test
	void Writer_IOException_발생() throws IOException {
		// given
		when(response.getWriter()).thenThrow(new IOException("Writer 실패"));

		// when & then
		assertThrows(IOException.class, () -> {
			entryPoint.commence(request, response, authException);
		});
	}
}
