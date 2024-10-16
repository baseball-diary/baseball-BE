package baseball.baseballDiary.auth.model;

import baseball.baseballDiary.auth.enums.SocialType;
import baseball.baseballDiary.common.model.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Social extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // 독립적인 PK
    private Long id;

    @Column(columnDefinition = "BINARY(16)", name = "member_cd")
    private UUID memberCd; // Member 엔티티와 연관된 외래 키

    @Enumerated(EnumType.STRING)
    @Column(name = "social_type")
    private SocialType socialType;

    @OneToOne
    @JoinColumn(name = "member_cd", insertable = false, updatable = false)
    private Member member;


    public void setSocialMember(Member member) {
        this.memberCd = member.getMemberCd();
        this.member = member;
    }
}