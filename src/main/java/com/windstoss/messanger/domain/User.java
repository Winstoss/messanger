package com.windstoss.messanger.domain;

import lombok.*;
import org.apache.commons.lang3.ObjectUtils;
import org.hibernate.annotations.Generated;
import org.hibernate.annotations.GenerationTime;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.UUID;

@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@Entity
@Table(name = "usr")
public class User {

    @Id
    @EqualsAndHashCode.Include
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    private String nickname;

    private String username;

    private String password;

    @Column(name = "phone_number")
    private String phoneNumber;

    private String bio;

    @Column(name = "avatar_path")
    private String avatarPath;

    @Generated(GenerationTime.INSERT)
    private Timestamp created_at;

    public User merge(User user) {
        nickname = ObjectUtils.defaultIfNull(user.getNickname(), nickname);
        username = ObjectUtils.defaultIfNull(user.getUsername(), username);
        password = ObjectUtils.defaultIfNull(user.getPassword(), password);
        phoneNumber = ObjectUtils.defaultIfNull(user.getPhoneNumber(), phoneNumber);
        bio = ObjectUtils.defaultIfNull(user.getBio(), bio);
        avatarPath = ObjectUtils.defaultIfNull(user.getAvatarPath(), avatarPath);
        return this;
    }

}
