package baseball.baseballDiary.auth.conf;

import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthCallback implements Serializable {

	private String code;

    private String state;

    private String authorization_code;

    private String oauth_token;

}
