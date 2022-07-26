package com.yamangulov.endpoint.repository;

import com.yamangulov.endpoint.entity.Contact;
import org.springframework.data.repository.CrudRepository;

import java.util.UUID;

public interface ContactRepository extends CrudRepository<Contact, UUID> {

}
