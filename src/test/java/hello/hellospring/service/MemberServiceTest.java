package hello.hellospring.service;

import hello.hellospring.domain.Member;
import hello.hellospring.repository.MemoryMemberRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

class MemberServiceTest {

    MemoryMemberRepository memberRepository;
    MemberService memberService;

    @BeforeEach
    public void beforeEach() {
        // static으로 선언을 해서 인스턴스와는 별개로 클래스로 접근을 할 수 있지만,
        // 같은 Repository 객체를 통해 테스트를 하기 위해서 MemberService에서
        // Service instance 생성시에 Repository를 주입해서 객체 인스턴스를 생성할 수 있도록 해야한다.
        memberRepository = new MemoryMemberRepository();
        memberService = new MemberService(memberRepository);
    }

    @AfterEach
    public void afterEach() {
        memberRepository.clearStore();
    }

    @Test
    void 회원가입() {
        //given
        Member member = new Member();
        member.setName("hello");

        //when
        Long saveId = memberService.join(member);

        //then
        // 제대로 회원가입이 되었는지 회원가입한 사용자의 아이디를 사용해서 찾는다.
        Member findMember = memberService.findOne(saveId).get();
        // 회원가입한 사용자의 이름이 서비스를 통해 찾은 사용자의 이름과 같으면 테스트 성공
        assertThat(member.getName()).isEqualTo(findMember.getName());
        // 테스트는 정상 플로우보다는 예외 플로우가 더 중요하다.
    }

    @Test
    public void 중복_회원_예외() {
        //given
        // member1과 member2를 같은 "spring"이라는 이름으로
        // name을 setup해서 객체를 만들고, memberService의 join메서드를 사용해서 가입을 한다.
        // 현재 프로젝트에서 같은 이름을 가진 사용자는 있을 수 없다는 가정을 했기 때문에
        Member member1 = new Member();
        member1.setName("spring");

        Member member2 = new Member();
        member2.setName("spring");

        //when
        memberService.join(member1);
        // member2가 회원가입하면 예외가 터져야 한다. (아래 try catch를 축약해서 아래와 같이 정리할 수 있다)
        IllegalStateException e = assertThrows(IllegalStateException.class, () -> memberService.join(member2));
        assertThat(e.getMessage()).isEqualTo("이미 존재하는 회원입니다.");
//        try {
//            memberService.join(member2);
//            // 테스트 예외가 발생하지 않고, 정상적으로 회원가입 된 후 넘어가면 실패이기 때문에
//            // fail() method를 실행한다.
//            fail();
//        } catch (IllegalStateException e) {
//            assertThat(e.getMessage()).isEqualTo("이미 존재하는 회원입니다.");
//        }
        //then
    }

    @Test
    void findMembers() {
    }

    @Test
    void findOne() {
    }
}