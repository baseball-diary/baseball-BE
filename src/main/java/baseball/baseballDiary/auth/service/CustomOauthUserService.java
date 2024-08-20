package baseball.baseballDiary.auth.service;

import baseball.baseballDiary.auth.domain.PrincipalDetails;
import baseball.baseballDiary.auth.info.GoogleOAuth2UserInfo;
import baseball.baseballDiary.auth.info.OAuth2UserInfo;
import baseball.baseballDiary.member.domain.Member;
import baseball.baseballDiary.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;


@Service
@RequiredArgsConstructor
public class CustomOauthUserService extends DefaultOAuth2UserService {

    private final MemberRepository memberRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest){

        // Oauth2 Provider 로 부터 사용자의 정보를 받아옴
        OAuth2User oAuth2User = super.loadUser(userRequest);
        OAuth2UserInfo oAuth2UserInfo = getUserInfo(userRequest.getClientRegistration().getRegistrationId(), oAuth2User.getAttributes());

        // 현재 정보 가져오기
        Optional<Member> member = memberRepository.findByNickname(oAuth2UserInfo.getName());

        if (member.isEmpty()) { // DB 에 없는 사용자라면 회원 가입 처리
            Member newMember = Member.builder()
                    .nickname(oAuth2UserInfo.getName())
                    .image_path(oAuth2UserInfo.getImageUrl())
                    .build();

            memberRepository.save(newMember);
            return new PrincipalDetails(newMember, oAuth2User.getAttributes());
        }else{ // DB 에 있는 사용자라면 변경된 정보 업데이트
            return new PrincipalDetails(member.get(), oAuth2User.getAttributes());
        }
    }

    private OAuth2UserInfo getUserInfo(String providerType, Map<String, Object> attributes){

        if(providerType.equals("google")){
            return new GoogleOAuth2UserInfo(attributes);
        }
        return null;
    }
}
