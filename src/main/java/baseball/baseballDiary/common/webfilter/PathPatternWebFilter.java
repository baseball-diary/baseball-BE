package baseball.baseballDiary.common.webfilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public abstract class PathPatternWebFilter extends OncePerRequestFilter {
    private static Logger logger = org.slf4j.LoggerFactory.getLogger(PathPatternWebFilter.class);

    private List<String> includePathPatterns = new ArrayList<>();
    private List<String> excludePathPatterns = new ArrayList<>();



    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String requestUri = request.getRequestURI();

        if (excludePathPatterns.stream().anyMatch(pattern -> pathMatches(pattern, requestUri))) {
            filterChain.doFilter(request, response);
            return;
        }

        if (includePathPatterns.stream().anyMatch(pattern -> pathMatches(pattern, requestUri))) {
            filterMatched(request, response, filterChain);
            return;
        }

        filterChain.doFilter(request, response);
    }

    protected boolean pathMatches(String pattern, String requestUri) {
        return new AntPathMatcher().match(pattern, requestUri);
    }

    protected void addIncludePathPatterns(String... patterns) {
        for (String pattern : patterns) {
            includePathPatterns.add(pattern);
        }
    }

    protected void addExcludePathPatterns(String... patterns) {
        for (String pattern : patterns) {
            excludePathPatterns.add(pattern);
        }
    }
    public abstract void filterMatched(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException;
}
