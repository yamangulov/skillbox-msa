package com.yamangulov.endpoint.repository;

import com.yamangulov.endpoint.entity.Profile;
import org.springframework.data.repository.CrudRepository;

import java.util.UUID;

public interface ProfileRepository extends CrudRepository<Profile, UUID> {
}
