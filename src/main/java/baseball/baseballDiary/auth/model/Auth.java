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

    @Schema(description = "accessToken")
    private String accessToken;
    @Schema(description = "refreshToken")
    private String refreshToken;
    @Schema(description = "token_type")
    private String token_type;
    @Schema(description = "expires_in")
    private String expires_in;

    @Schema(description = "기존 회원 맞음:true, 기존 회원 아님:false")
    private boolean memberExisted;

    private String email;

}