package baseball.baseballDiary.auth.controller;

import baseball.baseballDiary.auth.enums.SocialType;
import baseball.baseballDiary.auth.model.Auth;
import baseball.baseballDiary.auth.service.LoginService;
import baseball.baseballDiary.common.model.CustomResponse;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

    private final LoginService loginService;

    @GetMapping(value = "/social-login")
    public CustomResponse<Auth> kakaoAuthCallback(@RequestParam("code")String code,
                                                  @RequestParam("socialType") SocialType socialType,
                                                  @Parameter(hidden = true) HttpServletRequest request) {

        return CustomResponse.Ok(loginService.callback(code, socialType, request));
    }

}
