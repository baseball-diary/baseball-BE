package baseball.baseballDiary.auth.service;

import baseball.baseballDiary.auth.conf.AuthCallback;
import baseball.baseballDiary.auth.conf.AuthConfig;
import baseball.baseballDiary.auth.conf.AuthDefaultSource;
import baseball.baseballDiary.auth.model.Auth;
import baseball.baseballDiary.auth.model.Member;
import baseball.baseballDiary.auth.repository.MemberRepository;
import baseball.baseballDiary.auth.request.AuthLoginRequest;
import baseball.baseballDiary.common.constants.ApiState;
import baseball.baseballDiary.common.exception.CommonLogicException;
import baseball.baseballDiary.common.utils.JsonUtil;
import com.fasterxml.jackson.databind.JsonNode;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Map;

import static jakarta.xml.bind.JAXBIntrospector.getValue;

@Service
@Slf4j
@RequiredArgsConstructor
public class LoginService {

    private final MemberRepository memberRepository;

    public Auth callback(String code, String error, AuthConfig config, AuthDefaultSource source, HttpServletRequest request) throws CommonLogicException {

        Auth auth = null;

        if (StringUtils.isNotEmpty(error)) {
            log.error(error);
            throw new CommonLogicException(ApiState.AUTH_SNS.getCode(), ApiState.AUTH_SNS.getMessage());
        } else {

            try {
                AuthLoginRequest loginRequest = new AuthLoginRequest(config, source);
                Map<String, Object> tokenMap = loginRequest.getRequestToken(AuthCallback.builder().code(code).build());

                //parseSnsInfo
                auth = parseSnsInfo(tokenMap);

                //카카오가 사용중인지 체크
                auth = checkMemExistInfo(auth, request);

            } catch (CommonLogicException e) {
                log.error(e.getMessage());
                throw e;
            } catch (Exception e) {
                e.printStackTrace();
                log.error(e.getMessage());
                throw new CommonLogicException(ApiState.AUTH_SNS.getCode(), ApiState.AUTH_SNS.getMessage());
            }

        }

        return auth;
    }


    protected Auth checkMemExistInfo(Auth auth, HttpServletRequest request) throws CommonLogicException {

        //sns 연동 정보
        //MemberSns memberSns = snsMapper.checkSnsExist("KAKAO", auth.getMemberSns().getSnsId());

        if (!memberRepository.existsByEmailAndIsUse(auth.getEmail(), true)) {
            auth.setMemberExisted(false);
            return auth;
        } else {
            //sns 연동 정보 있음, 기존 회원 memberCd
            /*String memberCd = memberSns.getMemberCd();

            auth.getMemberSns().setMemberCd(memberCd);
            Member member = memberMapper.selectByMemberCd(memberCd);
            if (member != null) {
                //기존 회원 맞음:1
                auth.setMemberExisted("1");
                //로그인 & 토큰 생성
                AuthTokenDto authDto = makeAccessToken(member.getMemberCd(), keepLogin, serverRequest);
                auth.setAccessToken(authDto.getAccessToken());
                auth.setRefreshToken(authDto.getRefreshToken());
                auth.setInterestAge(member.getInterestAge());

                // FCM 토큰 갱신
                if (!StringUtils.isEmpty(auth.getFcm_token())) {
                    if (member.getFcmToken() == null || !member.getFcmToken().equals(auth.getFcm_token())) {
                        member.setFcmToken(auth.getFcm_token());
                        memberMapper.updateFcmToken(member);
                    }
                }

            } else {
                MemberInactive memberInactive = memberInactiveMapper.selectByPrimaryKey(memberCd);
                //휴면정보에도 없을시
                if (memberInactive == null) {
                    //기존 회원 아님:0
                    auth.setMemberExisted("0");
                } else {
                    //기존 회원 맞음:1
                    auth.setMemberExisted("1");
                    //휴면구분(휴면:0,탈퇴:1,삭제:2)
                    String inactiveType = memberInactive.getInactiveType();
                    //휴면
                    if (SystemSettingCode.INACTIVE_TYPE_SLEEP.getCode().equals(inactiveType)) {
                        throw new CommonLogicException(AccountApiState.USER_INACTIVE.getCode(), AccountApiState.USER_INACTIVE.getMsgCode());
                    }
                    //탈퇴 , 삭제
                    if (SystemSettingCode.INACTIVE_TYPE_WITHDRAWAL.getCode().equals(inactiveType)
                            || SystemSettingCode.INACTIVE_TYPE_DELETE.getCode().equals(inactiveType)) {
                        throw new CommonLogicException(ApiState.LOGIC.getCode(), "msg63_auth_withdrawalUser");
                    }
                }
            }*/
        }

        return auth;
    }

    protected Auth parseSnsInfo(Map<String, Object> tokenMap) throws Exception {

        log.info("tokenMap: " + tokenMap);
        Map<String, Object> resMap = JsonUtil.objectToMap(tokenMap.get("kakao_account"));
        Map<String, Object> propertiesMap = JsonUtil.objectToMap(tokenMap.get("properties"));

        return Auth.builder()
                .email(getValue(resMap.get("email")))
                .access_token(getValue(tokenMap.get("access_token")))
                .refresh_token(getValue(tokenMap.get("refresh_token")))
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

}
