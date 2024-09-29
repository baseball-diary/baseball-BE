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

        // registartionId 에 맞는 설정값 가져옴
        SocialLoginProperties.SocialProvider provider = socialLoginProperties.getProviders().get(registrationId);

        // 토큰 발급
        String accessToken = getAccessToken(code, provider);

        // 토큰 이용해서 유저 정보 발급
        JsonNode userResourceNode = getUserResource(accessToken, provider);

        if (registrationId.equals("naver")) {
            userResourceNode = userResourceNode.get("response");
        }

        String email = userResourceNode.get("email").asText();
        String nickname = registrationId + "_" + userResourceNode.get("name").asText();

        // JWT 토큰 반환
        return socialUser(nickname);
    }

    private String getAccessToken(String authorizationCode, SocialLoginProperties.SocialProvider provider) {
        return webClient.post()
                .uri(provider.tokenUri())  // token-uri 값 사용
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                .body(BodyInserters.fromFormData("code", authorizationCode)
                        .with("client_id", provider.clientId())  // client-id 값 사용
                        .with("client_secret", provider.clientSecret())  // client-secret 값 사용
                        .with("redirect_uri", provider.redirectUri())  // redirect-uri 값 사용
                        .with("grant_type", "authorization_code"))  // grant_type 값은 고정
                .retrieve()
                .bodyToMono(JsonNode.class)
                .map(responseNode -> responseNode.get("access_token").asText())
                .block();
    }

    private JsonNode getUserResource(String accessToken, SocialLoginProperties.SocialProvider provider) {
        return webClient.get()
                .uri(provider.resourceUri())
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
                .retrieve()
                .bodyToMono(JsonNode.class)
                .block();
    }

    private SocialMemberDto socialUser(String nickname) {
        return memberRepository.findByNickname(nickname)  // 닉네임으로 사용자 검색
                .map(member -> new SocialMemberDto(jwtProvider.generateLoginToken(member.getNickname())))  // 존재하는 경우, 토큰 생성 후 반환
                .orElseGet(() -> {  // 사용자가 없으면 새 멤버 생성
                    Member newMember = Member.builder().nickname(nickname).build();
                    memberRepository.save(newMember);  // DB에 저장
                    return new SocialMemberDto(jwtProvider.generateLoginToken(newMember.getNickname()));
                });
    }


}
