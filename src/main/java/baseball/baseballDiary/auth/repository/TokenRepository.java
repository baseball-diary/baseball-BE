package baseball.baseballDiary.auth.repository;

import baseball.baseballDiary.auth.domain.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TokenRepository extends JpaRepository<RefreshToken, String> {
}
