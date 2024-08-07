package baseball.baseballDiary.auth.model;

import baseball.baseballDiary.auth.enums.SocialType;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Social {

    @Id @GeneratedValue
    private Long socialId;

    @OneToOne
    @JoinColumn(name = "member_cd")
    private Member member;

    @Enumerated(EnumType.STRING)
    private SocialType type;

}
