package com.example.outsourcing_11.config.security;

import com.example.outsourcing_11.common.Status;
import com.example.outsourcing_11.domain.user.entity.User;
import com.example.outsourcing_11.domain.user.repository.UserRepository;
import com.example.outsourcing_11.util.JwtUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.context.SecurityContextHolder;

import java.io.IOException;
import java.util.Optional;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class JwtAuthenticationFilterTest {

    @Mock
    private JwtUtil jwtUtil;

    @Mock
    private UserRepository userRepository;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private FilterChain filterChain;

    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @BeforeEach
    void setUp() {
        jwtAuthenticationFilter = new JwtAuthenticationFilter(jwtUtil, userRepository);
        SecurityContextHolder.clearContext();
    }

    @Test
    void 토큰_없음_다음필터로넘김() throws ServletException, IOException {
        // given
        when(request.getHeader("Authorization")).thenReturn(null);

        // when
        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

        // then
        verify(filterChain).doFilter(request, response); // 다음 필터로 넘겼는지 확인
    }

    @Test
    void 토큰_유효하지않음_다음필터로넘김() throws ServletException, IOException {
        // given
        when(request.getHeader("Authorization")).thenReturn("invalid-token");
        when(jwtUtil.validateToken("invalid-token")).thenReturn(false);

        // when
        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

        // then
        verify(filterChain).doFilter(request, response); // 다음 필터로 넘겼는지 확인
    }

    @Test
    void 토큰_유효_유저인증_성공() throws ServletException, IOException {
        // given
        String token = "valid-token";
        Long userId = 1L;

        User user = mock(User.class);
        Status status = mock(Status.class);

        when(status.getValue()).thenReturn(true);
        when(user.getStatus()).thenReturn(status);
        when(user.getDeletedAt()).thenReturn(null);

        when(request.getHeader("Authorization")).thenReturn(token);
        when(jwtUtil.validateToken(token)).thenReturn(true);
        when(jwtUtil.extractUserId(token)).thenReturn(userId);
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        // when
        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

        // then
        verify(userRepository).findById(userId);
        verify(filterChain).doFilter(request, response);
    }
}
