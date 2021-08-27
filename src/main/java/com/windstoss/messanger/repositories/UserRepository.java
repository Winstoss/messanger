package com.windstoss.messanger.repositories;

import com.windstoss.messanger.domain.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Repository
public interface UserRepository extends CrudRepository<User, UUID> {

    Set<User> findByIdIn(Set<UUID> ids);

    Optional<User> findByUsername(String username);
}
