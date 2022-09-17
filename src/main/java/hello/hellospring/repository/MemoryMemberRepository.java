package hello.hellospring.repository;

import hello.hellospring.domain.Member;
import org.springframework.stereotype.Repository;

import java.util.*;

public class MemoryMemberRepository implements MemberRepository {
    // 회원아이디, 멤버 객체
    // 실무에서는 동시성 문제가 있을 수 있기 때문에 HashMap을 사용하지 않고, ConcurrentHashMap을 사용한다.

    private static Map<Long, Member> store = new HashMap<>();
    private static long sequence = 0L;

    @Override
    public Member save(Member member) {
        member.setId(++sequence);
        store.put(member.getId(), member);
        return member;
    }

    @Override
    public Optional<Member> findById(Long id) {
        return Optional.ofNullable(store.get(id));
    }

    @Override
    public Optional<Member> findByName(String name) {
        // 하나가 찾아지면 바로 반환을 하고(findAny), 하나도 찾지 못하면 Optional로 null을 감싸서 반환을 한다.
        return store.values().stream()
                .filter(member -> member.getName().equals(name))
                .findAny();
    }

    @Override
    public List<Member> findAll() {
        // Java에서 실무할때 리스트를 많이 사용한다.(loop)
        return new ArrayList<>(store.values());
    }

    public void clearStore() {
        store.clear();
    }
}
