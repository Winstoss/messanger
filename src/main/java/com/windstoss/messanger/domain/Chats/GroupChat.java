package com.windstoss.messanger.domain.Chats;

import com.windstoss.messanger.domain.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.ObjectUtils;
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
public class GroupChat {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    private String description;

    private String title;

    private String imagePath;

    @OneToOne
    @JoinColumn(
            name = "group_chat_creator",
                referencedColumnName =  "id"
    )
    private User creator;

    @OneToMany
    @JoinTable(
        name = "group_chat_admin",
            joinColumns = @JoinColumn(name = "group_chat_id"),
            inverseJoinColumns = @JoinColumn(name = "usr_id")
    )
    private Set<User> admins;

    @OneToMany
    @JoinTable(
        name = "group_chat_user",
            joinColumns = @JoinColumn(name = "group_chat_id"),
            inverseJoinColumns = @JoinColumn(name = "usr_id")
    )
    private Set<User> users;

    @Generated(GenerationTime.INSERT)
    private Timestamp creationTime;

    public GroupChat merge(GroupChat groupChat){
        description = ObjectUtils.defaultIfNull(groupChat.getDescription(), description);
        admins.addAll(groupChat.getAdmins());
        users.addAll(groupChat.getUsers());
        return this;
    }
}
