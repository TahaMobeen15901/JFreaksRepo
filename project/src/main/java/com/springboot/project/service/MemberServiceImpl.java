package com.springboot.project.service;

import com.springboot.project.DTO.MemberDTO;
import com.springboot.project.Dao.MemberRepository;
import com.springboot.project.entity.Member;
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
    public void saveMember(MemberDTO memberDTO){
        Member member = new Member(memberDTO.getUserName(), memberDTO.getName(), passwordEncoder.encode(memberDTO.getPassword()), memberDTO.getDob(), memberDTO.getPhone());
        memberRepository.save(member);
    }

    @Override
    @Transactional
    public void updateMember(MemberDTO memberDTO){
        Member member;
        member = memberRepository.findByUserName(memberDTO.getUserName());
        member.setName(memberDTO.getName());
        member.setPassword(memberDTO.getPassword());
        member.setEnabled(memberDTO.getEnabled());
        member.setDob(memberDTO.getDob());
        member.setPhone(memberDTO.getPhone());
        memberRepository.save(member);
    }


    @Override
    @Transactional
    public void deleteMember(Member member){
        memberRepository.delete(member);
    }

    @Override
    public Member findByUserName(String userName) {
        return memberRepository.findByUserName(userName);
    }

    @Override
    public List<MemberDTO> findAllMembers() {
      var members = memberRepository.findAll();
      return members.stream()
              .filter(member -> member.getRole().getRole().equals("ACTIVE"))
              .map(this::mapToMemberDTO)
              .collect(Collectors.toList());

    }

    private MemberDTO mapToMemberDTO(Member member) {
        return new MemberDTO(member.getUserName(),member.getName(),member.getPassword(), member.getEnabled(), member.getDob(), member.getPhone());
    }
}
