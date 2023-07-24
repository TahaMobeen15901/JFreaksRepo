package com.springboot.project.service;

import com.springboot.project.DTO.MemberDTO;
import com.springboot.project.Dao.MemberRepository;
import com.springboot.project.entity.Member;
import com.springboot.project.entity.Role;
import jakarta.transaction.Transactional;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;


import java.util.List;
import java.util.stream.Collectors;

@Validated
@Service
public class MemberServiceImpl implements MemberService {

    private MemberRepository memberRepository;

    private PasswordEncoder passwordEncoder;


    public MemberServiceImpl(MemberRepository memberRepository, PasswordEncoder passwordEncoder) {
        this.memberRepository = memberRepository;
        this.passwordEncoder = passwordEncoder;
    }
    @Override
    @Transactional
    public void saveMember(MemberDTO memberDTO) {
        Member member = new Member();
        member.setUserName(memberDTO.getUserName());
        member.setPassword(passwordEncoder.encode(memberDTO.getPassword()));
        member.setName(memberDTO.getName());
        member.setEnabled(1);
        member.setDob(memberDTO.getDob());
        member.setPhone(memberDTO.getPhone());
        member.setRole(new Role("ACTIVE", member));
        memberRepository.save(member);
        System.out.println("Member saved");
    }

    @Override
    @Transactional
    public void updateMember(MemberDTO memberDTO) {
        Member member = memberRepository.findByUserName(memberDTO.getUserName());
        member.setName(memberDTO.getName());
        member.setPassword(memberDTO.getPassword());
        member.setEnabled(memberDTO.getEnabled());
        member.setDob(memberDTO.getDob());
        member.setPhone(memberDTO.getPhone());

        memberRepository.save(member);
    }


    @Override
    @Transactional
    public void deleteMember(Member member) {
        memberRepository.delete(member);
    }

    @Override
    public Member findUserByUserName(String userName) {
        return memberRepository.findByUserName(userName);
    }

    @Override
    public List<MemberDTO> findAllMembers() {
        var members = memberRepository.findAll();
        return members.stream()
                .map(member->mapToMemberDTO(member))
                .collect(Collectors.toList());
    }

    private MemberDTO mapToMemberDTO(Member member) {
        MemberDTO memberDTO = new MemberDTO(member.getUserName(),member.getName(),member.getPassword(), member.getEnabled(), member.getDob(), member.getPhone());
        return memberDTO;
    }
}
