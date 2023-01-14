package com.clone.ohouse.community.entity;

import lombok.*;
//import org.springframework.security.crypto.password.PasswordEncoder;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name="user")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    Long id;
    @Column(nullable = false, length = 100, unique = true)
    private String email;
    @Column(nullable = false, length = 100, unique = true)
    private String password;
    @Column(nullable = false, length = 30)
    private String nickname;
    @Column(nullable = false, length = 50)
    private String phone;
    @Column(length = 30)
    private String birthday;
    @Column
    private Integer point;
    @Column(length = 1000)
    private String refreshToken;
    @Column
    private LocalDateTime regDate;
    @Builder
    public User(Long id, String email, String password, String nickname, String phone, String birthday,String refreshToken,Integer point,LocalDateTime regDate) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.nickname = nickname;
        this.phone = phone;
        this.birthday = birthday;
        this.refreshToken = refreshToken;
        this.point = point;
        this.regDate = regDate;
    }

    public User(String email, String password){
        this.email = email;
        this.password = password;
    }

    public void updateNickname(String nickname) {
        this.nickname = nickname;
    }

    public void updateUser(String email, String password) {
        this.email = email;
        this.password = password;
    }
    @OneToMany(mappedBy = "user",fetch = FetchType.EAGER)
//    @OrderBy("id asc")
    private List<Post> posts = new ArrayList<>();
    //    public void encodePassword(PasswordEncoder passwordEncoder){
//        this.password = passwordEncoder.encode(password);
//    }
    public void destroyToken() {
        this.refreshToken = null;
    }
}
