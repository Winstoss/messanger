package com.windstoss.messanger.domain.Chats;


import com.windstoss.messanger.domain.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "private_chat")
@Entity
public class PrivateChat  {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @OneToOne
    @JoinColumn(name = "firstUser_id", referencedColumnName = "id")
    private User firstUser;

    @OneToOne
    @JoinColumn(name = "secondUser_id", referencedColumnName = "id")
    private User secondUser;

}
