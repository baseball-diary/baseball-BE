package baseball.baseballDiary.member.domain;

import baseball.baseballDiary.member.enums.Gender;
import baseball.baseballDiary.member.repository.MemberRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class MemberTest {

    @Autowired
    MemberRepository memberRepository;

    @Test
    void testSoftDelete(){
        // Given
        Member member = Member.builder()
                .nickname("서원준")
                .age("27")
                .gender(Gender.M)
                .image_path("/temp.png")
                .build();

        memberRepository.save(member);

        // When
        UUID memberId = member.getId();
        memberRepository.deleteById(memberId);

        // Then
        Optional<Member> deleteMember = memberRepository.findById(memberId);
        assertThat(deleteMember).isPresent();
        assertThat(deleteMember.get().isDeleted()).isTrue();
    }
}