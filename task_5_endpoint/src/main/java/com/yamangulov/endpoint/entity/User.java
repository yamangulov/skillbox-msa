package com.yamangulov.endpoint.entity;


import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.util.Set;
import java.util.UUID;

@Entity
@Getter
@Setter
@ToString
@RequiredArgsConstructor
@SQLDelete(sql = "update users_scheme.\"user\" set deleted=true where id=?")
@Where(clause = "deleted = false")
public class User {
    @Id
    @GeneratedValue
    private UUID id;

    public User(Profile profile) {
        this.profile = profile;
    }

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "profile_id", referencedColumnName = "id")
    @JsonIdentityInfo(
            generator = ObjectIdGenerators.PropertyGenerator.class,
            property = "id")
    private Profile profile;

    @ManyToMany
    @JoinTable(
            name = "subscription",
            joinColumns = @JoinColumn(name = "subscriber_user_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "subscribed_user_id", referencedColumnName = "id"))
    private Set<User> subscription_from;

    @ManyToMany(mappedBy = "subscription_from")
    private Set<User> subscription_to;

    private Boolean deleted = Boolean.FALSE;
}
