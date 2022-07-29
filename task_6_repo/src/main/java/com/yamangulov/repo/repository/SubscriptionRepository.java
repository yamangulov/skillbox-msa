package com.yamangulov.repo.repository;

import com.yamangulov.repo.entity.Subscription;
import org.springframework.data.repository.CrudRepository;

import java.util.UUID;


public interface SubscriptionRepository extends CrudRepository<Subscription, UUID> {
    Subscription findBySubscriberUserIdAndSubscribedUserId(UUID subscriber_id, UUID subscribed_id);
}
