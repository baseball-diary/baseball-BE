package baseball.baseballDiary.auth.model;

import baseball.baseballDiary.auth.enums.Gender;
import baseball.baseballDiary.auth.enums.SocialType;
import baseball.baseballDiary.common.model.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import java.util.UUID;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Entity
public class Member extends BaseTimeEntity {

    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Column(columnDefinition = "BINARY(16)", name = "member_cd")
    private UUID memberCd;

    @Column(name = "nick_name")
    private String nickname;

    private String email;

    private String age;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    @Column(name = "is_use")
    private boolean isUse = true;

    @Column(name = "is_existed")
    private boolean isExisted = false;

    @Column(name = "image_path")
    private String imagePath;

}
