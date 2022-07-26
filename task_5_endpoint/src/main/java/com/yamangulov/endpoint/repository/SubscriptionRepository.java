package com.yamangulov.endpoint.repository;

import com.yamangulov.endpoint.entity.Subscription;
import org.springframework.data.repository.CrudRepository;

import java.util.UUID;


public interface SubscriptionRepository extends CrudRepository<Subscription, UUID> {
    Subscription findBySubscriberUserIdAndSubscribedUserId(UUID subscriber_id, UUID subscribed_id);
}
