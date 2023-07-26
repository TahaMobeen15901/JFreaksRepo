package com.springboot.project.service;

import com.springboot.project.DTO.MemberDTO;
import com.springboot.project.entity.Member;

import java.util.List;

public interface MemberService {

    void saveMember(MemberDTO memberDTO);

    void updateMember(MemberDTO memberDTO);

    void deleteMember(Member member);

    Member findByUserName(String userName);

    List<MemberDTO> findAllMembers();
}
