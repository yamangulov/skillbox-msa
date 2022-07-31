package com.yamangulov.repo.repository;

import com.yamangulov.repo.entity.Gender;
import org.springframework.data.repository.CrudRepository;

public interface GenderRepository extends CrudRepository<Gender, Integer> {
}
