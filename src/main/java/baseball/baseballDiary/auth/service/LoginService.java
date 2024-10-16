package baseball.baseballDiary.auth.service;

import baseball.baseballDiary.auth.conf.AuthCallback;
import baseball.baseballDiary.auth.conf.AuthConfig;
import baseball.baseballDiary.auth.conf.AuthDefaultSource;
import baseball.baseballDiary.auth.dto.AuthTokenDto;
import baseball.baseballDiary.auth.enums.SocialType;
import baseball.baseballDiary.auth.model.Auth;
import baseball.baseballDiary.auth.model.Member;
import baseball.baseballDiary.auth.model.Social;
import baseball.baseballDiary.auth.repository.MemberRepository;
import baseball.baseballDiary.auth.repository.SocialRepository;
import baseball.baseballDiary.auth.request.AuthLoginRequest;
import baseball.baseballDiary.auth.resolver.Account;
import baseball.baseballDiary.auth.utils.AccessTokenUtil;
import baseball.baseballDiary.common.config.BaseProfiles;
import baseball.baseballDiary.common.constants.ApiState;
import baseball.baseballDiary.common.constants.SystemSettingCode;
import baseball.baseballDiary.common.exception.CommonLogicException;
import baseball.baseballDiary.common.property.BaseProperty;
import baseball.baseballDiary.common.property.SocialProperties;
import baseball.baseballDiary.common.property.SocialProperty;
import baseball.baseballDiary.common.utils.IPUtil;
import baseball.baseballDiary.common.utils.JsonUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class LoginService {

    private final BaseProfiles profiles;
    private final BaseProperty property;

    private final MemberRepository memberRepository;
    private final SocialRepository socialRepository;

    private final SocialProperties socialProperties;

    public Auth callback(String code, SocialType socialType, HttpServletRequest request) throws CommonLogicException {

        AuthConfig config = null;
        SocialProperty socialConfig = null;
        AuthDefaultSource source = null;

        switch (socialType) {
            case KAKAO -> {
                socialConfig = socialProperties.kakao();
                source = AuthDefaultSource.KAKAO;
            }
            case NAVER -> {
                socialConfig = socialProperties.naver();
                //source = AuthDefaultSource.NAVER;
            }
        }

        if (socialConfig != null) {
            config = AuthConfig.builder()
                    .redirectUri(socialConfig.callbackUrl())
                    .clientId(socialConfig.clientId())
                    .clientSecret(socialConfig.clientSecret())
                    .build();
        }

        Auth auth = null;

        try {
            AuthLoginRequest loginRequest = new AuthLoginRequest(config, source);
            Map<String, Object> tokenMap = loginRequest.getRequestToken(AuthCallback.builder().code(code).build());
            //parseSnsInfo
            auth = parseSnsInfo(tokenMap);

            //카카오가 사용중인지 체크
            auth = checkMemExistInfo(auth, socialType, request);

        } catch (CommonLogicException e) {
            log.error(e.getMessage());
            throw e;
        } catch (Exception e) {
            e.printStackTrace();
            log.error(e.getMessage());
            throw new CommonLogicException(ApiState.AUTH_SNS.getCode(), ApiState.AUTH_SNS.getMessage());
        }

        return auth;
    }


    @Transactional
    protected Auth checkMemExistInfo(Auth auth, SocialType socialType, HttpServletRequest request) throws CommonLogicException {

        //sns 연동 정보
        //MemberSns memberSns = snsMapper.checkSnsExist("KAKAO", auth.getMemberSns().getSnsId());

        Optional<Member> member = memberRepository.findByEmailAndSocialType(auth.getEmail(), socialType);

        if (member.isEmpty()) {
            // 신규 가입
            Member newMember = Member.builder()
                    .email(auth.getEmail())
                    .build();
            memberRepository.save(newMember);

            Social social = Social.builder()
                            .socialType(socialType)
                            .memberCd(newMember.getMemberCd())
                            .member(newMember)
                            .build();
            socialRepository.save(social);

            /*회원가입 O => 추가정보 없어 isExisted = fasle => 프론트에서 추가정보 입력하는 페이지로 이동시키고 거기서 추가정보를 입력하게 만들거에요 => isExisted true
                        추가정보 있어 isExisted = true => 메인페이즈*/

            //auth.set
            auth.setMemberExisted(false);
            return auth;
        } else {
            //sns 연동 정보 있음, 기존 회원 memberCd
            UUID memberCd = member.get().getMemberCd();

            // 기존 회원 맞음:1
            // 추가정보 입력했는지 여부 판단
            auth.setMemberExisted(member.get().isExisted());
            //로그인 & 토큰 생성
            AuthTokenDto authDto = makeAccessToken(member, request);
            auth.setAccessToken(authDto.accessToken());
            auth.setRefreshToken(authDto.refreshToken());
        }

        return auth;
    }

    protected Auth parseSnsInfo(Map<String, Object> tokenMap) throws Exception {

        log.info("tokenMap: " + tokenMap);
        Map<String, Object> resMap = JsonUtil.objectToMap(tokenMap.get("kakao_account"));

        return Auth.builder()
                .email(getValue(resMap.get("email")))
                //.access_token(getValue(tokenMap.get("access_token")))
                //.refresh_token(getValue(tokenMap.get("refresh_token")))
                .token_type(getValue(tokenMap.get("token_type")))
                .expires_in(getValue(tokenMap.get("expires_in"))) //(초)
                .build();
    }

    private String getValue(Object obj) {
        if (obj != null) {
            return obj.toString();
        } else {
            return null;
        }
    }

    private AuthTokenDto makeAccessToken(Optional<Member> member, HttpServletRequest request) throws CommonLogicException {

        //사용자 IP
        String ip = IPUtil.getClientIp(request);

        //유효시간:분
        String expireMinutes = SystemSettingCode.ACCESS_TOKEN_TIME.getCode();
        // 로컬이나 개발환경에서는 세션유지 오래 되게 조건 추가
        if (profiles.isLocal() || profiles.isDev()) {
            expireMinutes =  SystemSettingCode.ACCESS_TOKEN_TIME_LONG.getCode();
        }
        log.info("expireMinutes : {}", expireMinutes);



        //token에 로그인 계정 정보 저장
        Account account = Account.builder()
                .memberCd(member.get().getMemberCd())
                .expireMinutes(expireMinutes)
                .ip(ip)
                .build();
        AccessTokenUtil.makeAuthToken(account, property, request);

        //redis에 로그인 정보 저장
        //Account accountMocked = AccessTokenUtil.accountMocked(account ,redisOperation);


        return new AuthTokenDto(account.getAccessToken(), account.getRefreshToken());
    }

}
