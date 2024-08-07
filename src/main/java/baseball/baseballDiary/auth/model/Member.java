package baseball.baseballDiary.auth.model;

import baseball.baseballDiary.auth.enums.Gender;
import baseball.baseballDiary.common.model.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Member extends BaseTimeEntity {

    @Id
    private String memberCd;

    private String nickname;

    private String email;

    private String age;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    private String imagePath;

    private boolean isUse = true;

    @OneToOne(mappedBy = "member", cascade = CascadeType.ALL)
    private Social social;

}
