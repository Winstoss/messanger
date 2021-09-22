package com.windstoss.messanger.domain.Chats;

import com.windstoss.messanger.domain.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.ObjectUtils;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.Generated;
import org.hibernate.annotations.GenerationTime;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Set;
import java.util.UUID;

@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
@Table(name = "group_chat")
@Entity
@DynamicInsert
@DynamicUpdate
public class GroupChat {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    private String title;

    @Column(name = "image_path")
    private String imagePath;

    @OneToOne
    @JoinColumn(
            name = "creator_id",
                referencedColumnName =  "id"
    )
    private User creator;

    @OneToMany
    @JoinTable(
        name = "group_chat_admin",
            joinColumns = @JoinColumn(name = "chat_id"),
            inverseJoinColumns = @JoinColumn(name = "admin_id")
    )
    private Set<User> admins;

    @OneToMany
    @JoinTable(
        name = "group_chat_user",
            joinColumns = @JoinColumn(name = "chat_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    private Set<User> users;

    @Generated(GenerationTime.INSERT)
    @Column(name = "created_at")
    private Timestamp createdAt;

    public GroupChat merge(GroupChat groupChat){
        imagePath = ObjectUtils.defaultIfNull(groupChat.getImagePath(), title);
        title = ObjectUtils.defaultIfNull(groupChat.getTitle(), title);
        return this;
    }

}
