package com.yamangulov.repo.entity;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import java.util.UUID;

@Entity
@Getter
@Setter
@RequiredArgsConstructor
@ToString
@SQLDelete(sql = "update public.contact set deleted=true where id=?")
@Where(clause = "deleted = false")
public class Contact {
    @Id
    @GeneratedValue
    private UUID id;

    private String email;

    private String phone;

    @OneToOne(mappedBy = "contact")
    @JsonIdentityInfo(
            generator = ObjectIdGenerators.PropertyGenerator.class,
            property = "id")
    Profile profile;

    public Contact(String email, String phone) {
        this.email = email;
        this.phone = phone;
    }

    public Contact(UUID id, String email, String phone) {
        this.id = id;
        this.email = email;
        this.phone = phone;
    }

    private Boolean deleted = Boolean.FALSE;
}
