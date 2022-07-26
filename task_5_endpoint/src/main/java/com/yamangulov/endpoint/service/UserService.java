package com.yamangulov.endpoint.service;

import com.yamangulov.endpoint.entity.Contact;
import com.yamangulov.endpoint.entity.Profile;
import com.yamangulov.endpoint.entity.Subscription;
import com.yamangulov.endpoint.entity.User;
import com.yamangulov.endpoint.repository.ContactRepository;
import com.yamangulov.endpoint.repository.ProfileRepository;
import com.yamangulov.endpoint.repository.SubscriptionRepository;
import com.yamangulov.endpoint.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Map;
import java.util.UUID;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final ProfileRepository profileRepository;
    private final ContactRepository contactRepository;

    private final SubscriptionRepository subscriptionRepository;

    public UserService(UserRepository userRepository, ProfileRepository profileRepository, ContactRepository contactRepository, SubscriptionRepository subscriptionRepository) {
        this.userRepository = userRepository;
        this.profileRepository = profileRepository;
        this.contactRepository = contactRepository;
        this.subscriptionRepository = subscriptionRepository;
    }

    public String creatUser(Map<String, String> requestParams) {
        Contact contact = contactRepository.save(
                new Contact(
                        requestParams.get("email"),
                        requestParams.get("phone"))
        );

        Profile profile = profileRepository.save(
                new Profile(
                        requestParams.get("name"),
                        requestParams.get("surname"),
                        requestParams.get("birthDate"),
                        contact
                )
        );
        User savedUser = userRepository.save(
                new User(
                        profile
                )
        );
        return String.format(
                "Пользователь %s добавлен в БД с id = %s",
                savedUser.getProfile().getSurname(),
                savedUser.getId()
        );
    }

    public User getUser(UUID id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    public String updateUser(Map<String, String> requestParams, UUID id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        Profile profile = user.getProfile();
        Contact contact = profile.getContact();
        requestParams.forEach((key, value) -> {
            if (key.equals("name")) {
                profile.setName(value);
            }
            if (key.equals("surname")) {
                profile.setSurname(value);
            }
            if (key.equals("secondName")) {
                profile.setSecondName(value);
            }
            if (key.equals("birthDate")) {
                LocalDate date = LocalDate.parse(value);
                profile.setBirthDate(
                        date.atStartOfDay(ZoneId.of("Europe/Moscow")).toInstant()
                );
            }
            if (key.equals("avatarLink")) {
                profile.setAvatarLink(value);
            }
            if (key.equals("information")) {
                profile.setInformation(value);
            }
            if (key.equals("email")) {
                contact.setEmail(value);
            }
            if (key.equals("phone")) {
                contact.setPhone(value);
            }
        });
        contactRepository.save(contact);
        profileRepository.save(profile);
        return String.format(
                "Данные пользователя %s с id = %s успешно обновлены в БД",
                user.getProfile().getSurname(),
                user.getId()
        );
    }

    public String deleteUser(UUID id) {
        User user = userRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        userRepository.deleteById(user.getId());
        return String.format(
                "Пользователя %s с id = %s успешно удален из БД",
                user.getProfile().getSurname(),
                user.getId()
        );
    }

    public String subscribe(UUID subscriber_id, UUID subscribed_id) {
        User subsriber = userRepository.findById(subscriber_id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        User subsribed = userRepository.findById(subscribed_id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        subscriptionRepository.save(
                new Subscription(
                    subscriber_id,
                    subscribed_id
                )
        );
        return String.format(
                "Пользователm %s с id = %s успешно подписан на пользователя %s с id = %s",
                subsriber.getProfile().getSurname(),
                subsriber.getId(),
                subsribed.getProfile().getSurname(),
                subsribed.getId()
        );
    }

    public String unsubscribe(UUID subscriber_id, UUID subscribed_id) {
        User subsriber = userRepository.findById(subscriber_id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        User subsribed = userRepository.findById(subscribed_id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        subscriptionRepository.delete(subscriptionRepository.findBySubscriberUserIdAndSubscribedUserId(subscriber_id, subscribed_id));
        return String.format(
                "Пользователm %s с id = %s успешно отписан на пользователя %s с id = %s",
                subsriber.getProfile().getSurname(),
                subsriber.getId(),
                subsribed.getProfile().getSurname(),
                subsribed.getId()
        );
    }
}
