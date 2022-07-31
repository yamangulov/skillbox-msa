package com.yamangulov.repo.repository;

import com.yamangulov.repo.entity.User;
import org.springframework.data.repository.CrudRepository;

import java.util.UUID;

public interface UserRepository extends CrudRepository<User, UUID> {
}
