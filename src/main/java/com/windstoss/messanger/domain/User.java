package com.windstoss.messanger.domain;

import com.windstoss.messanger.utils.StringUtils;
import lombok.*;
import org.apache.commons.lang3.ObjectUtils;
import org.hibernate.annotations.Generated;
import org.hibernate.annotations.GenerationTime;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Objects;
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
        password = StringUtils.defaultIfEmpty(user.getPassword(), this.password);
        phoneNumber = ObjectUtils.defaultIfNull(user.getPhoneNumber(), phoneNumber);
        bio = ObjectUtils.defaultIfNull(user.getBio(), bio);
        avatarPath = ObjectUtils.defaultIfNull(user.getAvatarPath(), avatarPath);
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(id, user.id) && Objects.equals(nickname, user.nickname) && Objects.equals(username, user.username) && Objects.equals(password, user.password) && phoneNumber.equals(user.phoneNumber) && bio.equals(user.bio) && avatarPath.equals(user.avatarPath) && Objects.equals(created_at, user.created_at);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, nickname, username, password, phoneNumber, bio, avatarPath, created_at);
    }
}
