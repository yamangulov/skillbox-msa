package com.yamangulov.repo.repository;

import com.yamangulov.repo.entity.Contact;
import org.springframework.data.repository.CrudRepository;

import java.util.UUID;

public interface ContactRepository extends CrudRepository<Contact, UUID> {

}
