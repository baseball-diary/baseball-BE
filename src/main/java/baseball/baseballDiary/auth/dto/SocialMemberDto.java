package baseball.baseballDiary.auth.dto;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class SocialMemberDto {
    private MemberTokenDto token;
    private String role;
    private String nickname;
}