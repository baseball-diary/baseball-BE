package baseball.baseballDiary.auth;

import baseball.baseballDiary.auth.domain.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TokenRepository extends JpaRepository<RefreshToken, String> {
}
