package baseball.baseballDiary.auth.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@Schema
public class Auth {

    @Schema(description = "sns_access_token")
    private String access_token;
    @Schema(description = "sns_refresh_token")
    private String refresh_token;
    @Schema(description = "sns_token_type")
    private String token_type;
    @Schema(description = "sns_expires_in")
    private String expires_in;

    @Schema(description = "기존 회원 맞음:true, 기존 회원 아님:false")
    private boolean memberExisted;

    @Schema(description = "accessToken")
    private String accessToken;

    @Schema(description = "refreshToken")
    private String refreshToken;

    private String email;

}
