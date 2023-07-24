package com.springboot.project.Dao;

import com.springboot.project.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, String> {
    Member findByUserName(String userName);
}
