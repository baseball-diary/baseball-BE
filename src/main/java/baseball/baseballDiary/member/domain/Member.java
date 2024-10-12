package baseball.baseballDiary.member.domain;

import baseball.baseballDiary.member.enums.Gender;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Getter
@SQLDelete(sql = "UPDATE member SET deleted = true WHERE member_id = ?")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "member_id", updatable = false)
    private UUID id;

    private String email;
    private String age;
    private Gender gender;
    private String image_path;
    private LocalDateTime last_login_dt;
    private boolean deleted = Boolean.FALSE;

    @Builder
    public Member(String email, String age, Gender gender, String image_path) {
        this.email = email;
        this.age = age;
        this.gender = gender;
        this.image_path = image_path;
    }
}
