package sullog.backend.member.service;

import org.springframework.stereotype.Service;
import sullog.backend.member.entity.Member;
import sullog.backend.member.mapper.MemberMapper;

@Service
public class MemberService {

    private MemberMapper memberMapper;

    public MemberService(MemberMapper memberMapper) {
        this.memberMapper = memberMapper;
    }

    public void registerMember(Member member) {
        if(isAlreadyRegisteredMember(member)) {
            return;
        }

        memberMapper.insertMember(member);
    }

    private boolean isAlreadyRegisteredMember(Member member) {
        return memberMapper.selectMemberByEmail(member.getEmail()) != null;
    }

    public Member findMemberByEmail(String email) {
        return memberMapper.selectMemberByEmail(email);
    }

    public void deleteMember(int memberId) {
        memberMapper.deleteMemberById(memberId);
    }
}
