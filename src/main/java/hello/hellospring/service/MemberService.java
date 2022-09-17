package hello.hellospring.service;

import hello.hellospring.domain.Member;
import hello.hellospring.repository.MemberRepository;
import hello.hellospring.repository.MemoryMemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Transactional
public class MemberService {
    private final MemberRepository memberRepository;

    @Autowired
    public MemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    public Long join(Member member) {
        // 중복회원 검사
        validateDuplicateMember(member);

        // 같은 이름이 있는 회원은 안된다. (중복회원)
        memberRepository.save(member);
        return member.getId();
    }

    private void validateDuplicateMember(Member member) {
        // null일 가능성이 있는 값의 경우에는 Optional로 Member 객체를 감싸서
        // result.get() 직접 꺼내는 것을 권장하지 않는다. orElseGet()를 잘 사용한다.
        // ifPresent 메서드를 사용한다.(Optional이 다양한 메서드를 제공)
        memberRepository.findByName(member.getName())
                .ifPresent(m -> {
                    throw new IllegalStateException("이미 존재하는 회원입니다.");
                });
    }

    // 전체 회원 조회
    public List<Member> findMembers() {
        return memberRepository.findAll();
    }

    //
    public Optional<Member> findOne(Long memberId) {
        return memberRepository.findById(memberId);
    }
}
