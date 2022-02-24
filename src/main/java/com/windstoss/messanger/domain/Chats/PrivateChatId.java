//package com.windstoss.messanger.domain.Chats;
//
//import lombok.NoArgsConstructor;
//
//import javax.persistence.Embeddable;
//import java.io.Serializable;
//import java.util.Objects;
//import java.util.UUID;
//
//
//@NoArgsConstructor
//public class PrivateChatId implements Serializable {
//
//    private UUID firstUser;
//
//    private UUID secondUser;
//
//    public PrivateChatId(UUID firstUserId, UUID secondUserId){
//        this.firstUser = firstUserId;
//        this.secondUser = secondUserId;
//    }
//
//    @Override
//    public boolean equals(Object o) {
//        if (this == o) return true;
//        if (o == null || getClass() != o.getClass()) return false;
//        PrivateChatId that = (PrivateChatId) o;
//        return firstUser.equals(that.firstUser) && secondUser.equals(that.secondUser);
//    }
//
//    @Override
//    public int hashCode() {
//        return Objects.hash(firstUser, secondUser);
//    }
//}
