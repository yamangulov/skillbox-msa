package com.yamangulov.repo.repository;

import com.yamangulov.repo.entity.HardSkills;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;
import java.util.UUID;

public interface HardSkillsRepository extends CrudRepository<HardSkills, UUID> {
    Optional<HardSkills> findByUserIdAndSkillId(UUID user_id, Integer skill_id);
}
