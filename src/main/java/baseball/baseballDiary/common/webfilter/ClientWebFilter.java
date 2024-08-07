package baseball.baseballDiary.common.webfilter;

import baseball.baseballDiary.common.constants.ApiState;
import baseball.baseballDiary.common.exception.CommonLogicException;
import baseball.baseballDiary.common.property.BaseProperty;
import baseball.baseballDiary.common.utils.ExceptionHandlerUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Slf4j
@Component
public class ClientWebFilter extends PathPatternWebFilter {
    private final String clientId;
    private final ExceptionHandlerUtil exceptionHandlerUtil;

    public ClientWebFilter(BaseProperty baseProperty, ExceptionHandlerUtil exceptionHandlerUtil) {
        this.clientId = baseProperty.getClientId();
        this.exceptionHandlerUtil = exceptionHandlerUtil;

        //this.addIncludePathPatterns("/*/baseball-diary/api/**");
        this.addIncludePathPatterns("/*/back_login/api/v1/**");
        this.addExcludePathPatterns(
                "/",
                "/css/**",
                "/fonts/**",
                "/images/**",
                "/js/**");
    }

    @Override
    public void filterMatched(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            checkClient(request);
        } catch (Exception e) {
            ObjectMapper objectMapper = new ObjectMapper();
            ResponseEntity responseEntity = exceptionHandlerUtil.renderErrorResponse(e);
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write(objectMapper.writeValueAsString(responseEntity.getBody()));
            return;
        }
        filterChain.doFilter(request, response);
    }

    public boolean checkClient(HttpServletRequest request) throws Exception {

        log.info("preHandle path : {}", request.getRequestURI());

        // check client id
        String requestClientId = request.getHeader("clientId");

        if (requestClientId != null && requestClientId.equals(clientId)) {
            return true;
        }

        // client id is wrong
        log.warn("client id is wrong : {}", requestClientId);

        throw new CommonLogicException(ApiState.CLIENT.getCode(), ApiState.CLIENT.getMessage());
    }
}
