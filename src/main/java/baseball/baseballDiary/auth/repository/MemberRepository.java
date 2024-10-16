package baseball.baseballDiary.auth.repository;

import baseball.baseballDiary.auth.enums.SocialType;
import baseball.baseballDiary.auth.model.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, String> {

    Optional<Member> findByEmail(String email);

    @Query("SELECT m FROM Member m join Social s on m.memberCd = s.memberCd WHERE m.email = :email AND s.socialType = :socialType")
    Optional<Member> findByEmailAndSocialType(@Param("email") String email, @Param("socialType") SocialType socialType);
}
