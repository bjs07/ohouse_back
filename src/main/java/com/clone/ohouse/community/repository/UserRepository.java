package com.clone.ohouse.community.repository;


import com.clone.ohouse.community.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
@Repository

public interface  UserRepository extends JpaRepository<User,Long> {
    Optional<User> findByEmail(String email);
    public List<User> findAll();
    public User findOne(Long id);
    public boolean store(User user);
    public boolean delete(Long id);
    // 이메일 중복 검사
    // select count(*) from tbl_user where email=?
//    @Query("select count(*) from UserEntity u where u.email=?1")
    boolean existsByEmail(String email);
}
