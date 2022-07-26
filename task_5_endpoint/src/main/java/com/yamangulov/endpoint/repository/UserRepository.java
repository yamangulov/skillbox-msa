package com.yamangulov.endpoint.repository;

import com.yamangulov.endpoint.entity.User;
import org.springframework.data.repository.CrudRepository;

import java.util.UUID;

public interface UserRepository extends CrudRepository<User, UUID> {
}
