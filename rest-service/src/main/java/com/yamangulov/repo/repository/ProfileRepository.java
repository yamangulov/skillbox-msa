package com.yamangulov.repo.repository;

import com.yamangulov.repo.entity.Profile;
import org.springframework.data.repository.CrudRepository;

import java.util.UUID;

public interface ProfileRepository extends CrudRepository<Profile, UUID> {
}
