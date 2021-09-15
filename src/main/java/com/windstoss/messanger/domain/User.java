package com.windstoss.messanger.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.ObjectUtils;
import org.hibernate.annotations.Generated;
import org.hibernate.annotations.GenerationTime;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@Entity
@Table(name = "usr")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    private String nickname;

    private String username;

    private String password;

    private String phone_number;

    private String bio;

    private String avatar_path;

    @Generated(GenerationTime.INSERT)
    private Timestamp created_at;

    public User merge(User user) {
        nickname = ObjectUtils.defaultIfNull(user.getNickname(), nickname);
        username = ObjectUtils.defaultIfNull(user.getUsername(), username);
        password = ObjectUtils.defaultIfNull(user.getPassword(), password);
        phone_number = ObjectUtils.defaultIfNull(user.getPhone_number(), phone_number);
        bio = ObjectUtils.defaultIfNull(user.getBio(), bio);
        avatar_path = ObjectUtils.defaultIfNull(user.getAvatar_path(), avatar_path);
        return this;
    }

}
