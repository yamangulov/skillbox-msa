package com.yamangulov.repo.entity;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.UUID;

@Entity
@Getter
@Setter
@ToString
@RequiredArgsConstructor
@SQLDelete(sql = "update users_scheme.profile set deleted=true where id=?")
@Where(clause = "deleted = false")
public class Profile {
    @Id
    @GeneratedValue
    private UUID id;

    private String name;

    private String surname;

    @Column(name = "second_name")
    private String secondName;

    @Column(name = "birth_date")
    private Instant birthDate;

    @Column(name = "avatar_link")
    private String avatarLink;

    private String information;

    @OneToOne
    @JoinColumn(name = "city_id")
    private City city;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "contact_id", referencedColumnName = "id")
    @JsonIdentityInfo(
            generator = ObjectIdGenerators.PropertyGenerator.class,
            property = "id")
    private Contact contact;

    @OneToOne
    @JoinColumn(name = "gender_id")
    private Gender gender;

    @OneToOne(mappedBy = "profile")
    User user;

    public Profile(String name, String surname, String birthDate, Contact contact) {
        this.name = name;
        this.surname = surname;
        LocalDate date = LocalDate.parse(birthDate);
        this.birthDate = date.atStartOfDay(ZoneId.of("Europe/Moscow")).toInstant();
        this.contact = contact;
    }

    private Boolean deleted = Boolean.FALSE;
}
