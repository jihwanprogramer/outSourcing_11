package com.example.outsourcing_11.config.security;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.io.PrintWriter;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.access.AccessDeniedException;

@ExtendWith(MockitoExtension.class)
class JwtAccessDeniedHandlerTest {

	@InjectMocks
	private JwtAccessDeniedHandler handler;

	@Mock
	private HttpServletRequest request;

	@Mock
	private HttpServletResponse response;

	@Mock
	private PrintWriter writer;

	private AccessDeniedException accessDeniedException;

	@BeforeEach
	void setUp() {
		accessDeniedException = new AccessDeniedException("접근 거부");
	}

	@Test
	void 권한없음_정상처리() throws IOException {
		// given
		when(response.getWriter()).thenReturn(writer);

		// when
		handler.handle(request, response, accessDeniedException);

		// then
		verify(response).setStatus(HttpServletResponse.SC_FORBIDDEN); // 403 확인
		verify(response).setContentType("application/json; charset=UTF-8");
		verify(writer).write("{\"message\": \"권한이 없습니다. 관리자에게 문의하세요.\"}");
	}

	@Test
	void Writer_IOException_발생() throws IOException {
		// given
		when(response.getWriter()).thenThrow(new IOException("Writer 실패"));

		// when & then
		assertThrows(IOException.class, () -> {
			handler.handle(request, response, accessDeniedException);
		});
	}
}
