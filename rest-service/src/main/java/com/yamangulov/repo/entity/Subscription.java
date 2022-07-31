package com.yamangulov.repo.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.UUID;

@Entity
@Getter
@Setter
@ToString
@RequiredArgsConstructor
public class Subscription {
    @Id
    @GeneratedValue
    private UUID id;

    @Column(name = "subscriber_user_id")
    private UUID subscriberUserId;
    @Column(name = "subscribed_user_id")
    private UUID subscribedUserId;

    public Subscription(UUID subscriberUserId, UUID subscribedUserId) {
        this.subscriberUserId = subscriberUserId;
        this.subscribedUserId = subscribedUserId;
    }
}
