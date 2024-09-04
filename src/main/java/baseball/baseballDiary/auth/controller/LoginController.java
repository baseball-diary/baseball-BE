package baseball.baseballDiary.auth.controller;


import baseball.baseballDiary.auth.dto.SocialMemberDto;
import baseball.baseballDiary.auth.service.LoginService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/auth")
public class LoginController {

    private final LoginService loginService;

    @PostMapping("/login")
    public ResponseEntity<SocialMemberDto> googleLogin(@RequestParam("code") String code, @RequestParam("registrationId") String registrationId) {

        return ResponseEntity.ok().body(loginService.socialLogin(code, registrationId));
    }
}
