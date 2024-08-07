package baseball.baseballDiary.auth.controller;

import baseball.baseballDiary.auth.conf.AuthConfig;
import baseball.baseballDiary.auth.conf.AuthDefaultSource;
import baseball.baseballDiary.auth.model.Auth;
import baseball.baseballDiary.auth.request.AuthLoginRequest;
import baseball.baseballDiary.auth.service.LoginService;
import baseball.baseballDiary.common.exception.CommonLogicException;
import baseball.baseballDiary.common.model.CustomResponse;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@Tag(name = "auth-user")
@RequestMapping(path = "/back_login/api/v1/auth")
@RestController
@RequiredArgsConstructor
public class LoginController {

    @Value("${auth.client.kakao.client-id}")
    private String kakao_clientId;

    @Value("${auth.client.kakao.client-secret}")
    private String kakao_clientSecret;

    private final LoginService loginService;

    @GetMapping(value = "/kakao-auth-path")
    public CustomResponse<String> kakaoAuthPath(@RequestParam("loginCallbackUrl") String loginCallbackUrl) throws CommonLogicException {

        return CustomResponse.Ok(new AuthLoginRequest(
                                                    AuthConfig.builder().
                                                        redirectUri(loginCallbackUrl).
                                                        clientId(kakao_clientId).
                                                        clientSecret(kakao_clientSecret).build(),
                                                    AuthDefaultSource.KAKAO).authorize()
        );
    }

    @GetMapping(value = "/kakao-callback")
    public CustomResponse<Auth> kakaoAuthCallback(@RequestParam("code")String code,
                                                  @RequestParam(value = "error", required = false) String error,
                                                  @RequestParam("loginCallbackUrl") String loginCallbackUrl,
                                                  @Parameter(hidden = true) HttpServletRequest request) {

        return CustomResponse.Ok(loginService.callback(code, error,
                                                        AuthConfig.builder()
                                                                .redirectUri(loginCallbackUrl)
                                                                .clientId(kakao_clientId)
                                                                .clientSecret(kakao_clientSecret).build()
                                                        , AuthDefaultSource.KAKAO, request));
        //return CustomResponse.Ok(null);
    }

}
