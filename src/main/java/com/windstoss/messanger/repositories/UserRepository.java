package com.windstoss.messanger.repositories;

import com.windstoss.messanger.domain.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public interface UserRepository extends CrudRepository<User, UUID> {

    Set<User> findUsersByIdIn(Set<UUID> ids);

    Set<User> findUsersByUsernameIn(Collection<String> username);

    Optional<User> findUserByUsername(String username);

    @Query(value = "FROM User as u " +
            "WHERE LOWER(u.nickname) LIKE LOWER(CONCAT(:username, '%'))")
    List<User> userSearch(String username);
}
