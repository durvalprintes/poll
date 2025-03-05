package dev.printes.poll.config;

import java.io.IOException;
import java.util.Optional;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;

import dev.printes.poll.service.AssociateService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class ApiKeyFilter  extends GenericFilterBean {

    private static final String X_API_KEY = "X-API-KEY";

    private final AssociateService associateDetailsService;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        var httpRequest = (HttpServletRequest) request;
        var httpResponse = (HttpServletResponse) response;

        try {
            Optional.ofNullable(httpRequest.getHeader(X_API_KEY)).ifPresent(apiKey -> {
                var userDetails = associateDetailsService.loadUserByUsername(apiKey);
                var authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(authentication);
            });
        } catch (Exception e) {
            log.error("Error: {}", e.getMessage(), e);
            this.sendForbiddenResponse(httpResponse);
            return;
        }

        chain.doFilter(request, response);
    }

    private void sendForbiddenResponse(HttpServletResponse response) throws IOException {
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        response.setContentType("application/json");
        response.getWriter().write("""
            { "error": "Invalid API Key" }
            """);
    }

}
