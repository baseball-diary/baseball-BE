package baseball.baseballDiary.auth.repository;

import baseball.baseballDiary.auth.model.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, String> {

    boolean existsByEmailAndIsUse(String email, boolean isUse);
}
