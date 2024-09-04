package baseball.baseballDiary.auth.service;

import baseball.baseballDiary.auth.config.SocialLoginProperties;
import baseball.baseballDiary.auth.dto.SocialMemberDto;
import baseball.baseballDiary.auth.token.JwtProvider;
import baseball.baseballDiary.member.domain.Member;
import baseball.baseballDiary.member.repository.MemberRepository;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class LoginService {

    private final SocialLoginProperties socialLoginProperties;
    private final WebClient webClient = WebClient.builder().build();
    private final MemberRepository memberRepository;
    private final JwtProvider jwtProvider;

    public SocialMemberDto socialLogin(String code, String registrationId) {

        // 토큰 발급
        String accessToken = getAccessToken(code, registrationId);

        // 토큰 이용해서 유저 정보 발급
        JsonNode userResourceNode = getUserResource(accessToken, registrationId);

        if (registrationId.equals("naver")) {
            userResourceNode = userResourceNode.get("response");
        }

        String email = userResourceNode.get("email").asText();
        String nickname = registrationId + "_" + userResourceNode.get("name").asText();

        // JWT 토큰 반환
        return socialUser(nickname);
    }

    private String getAccessToken(String authorizationCode, String registrationId) {
        return webClient.post()
                .uri(socialLoginProperties.getGoogle().get("token-uri"))
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                .body(BodyInserters.fromFormData("code", authorizationCode)
                        .with("client_id", socialLoginProperties.getGoogle().get("client-id"))
                        .with("client_secret", socialLoginProperties.getGoogle().get("client-secret"))
                        .with("redirect_uri", socialLoginProperties.getGoogle().get("redirect-uri"))
                        .with("grant_type", "authorization_code"))
                .retrieve()
                .bodyToMono(JsonNode.class)
                .map(responseNode -> responseNode.get("access_token").asText())
                .block();
    }

    private JsonNode getUserResource(String accessToken, String registrationId) {
        return webClient.get()
                .uri(socialLoginProperties.getGoogle().get("resource-uri"))
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
                .retrieve()
                .bodyToMono(JsonNode.class)
                .block();
    }

    private SocialMemberDto socialUser(String nickname) {
        Optional<Member> member = memberRepository.findByNickname(nickname);
        SocialMemberDto socialMemberDto = new SocialMemberDto();

        if (member.isEmpty()) {  //  처음 로그인
            Member newMember = Member.builder()
                    .nickname(nickname)
                    .build();

            // DB 에 저장
            memberRepository.save(newMember);

            // nickname 으로 jwt 생성
            socialMemberDto.setToken(jwtProvider.generateLoginToken(newMember.getNickname()));
        }else{
            socialMemberDto.setToken(jwtProvider.generateLoginToken(member.get().getNickname()));
        }

        return socialMemberDto;
    }


}
