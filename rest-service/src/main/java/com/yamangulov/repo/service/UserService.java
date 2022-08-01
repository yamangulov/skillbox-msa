package com.yamangulov.repo.service;

import com.yamangulov.repo.entity.*;
import com.yamangulov.repo.repository.*;
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

    private final SkillRepository skillRepository;

    private final HardSkillsRepository hardSkillsRepository;

    public UserService(UserRepository userRepository, ProfileRepository profileRepository, ContactRepository contactRepository, SubscriptionRepository subscriptionRepository, SkillRepository skillRepository, HardSkillsRepository hardSkillsRepository) {
        this.userRepository = userRepository;
        this.profileRepository = profileRepository;
        this.contactRepository = contactRepository;
        this.subscriptionRepository = subscriptionRepository;
        this.skillRepository = skillRepository;
        this.hardSkillsRepository = hardSkillsRepository;
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
        User subscriber = userRepository.findById(subscriber_id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        User subscribed = userRepository.findById(subscribed_id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        subscriptionRepository.save(
                new Subscription(
                    subscriber_id,
                    subscribed_id
                )
        );
        return String.format(
                "Пользователь %s с id = %s успешно подписан на пользователя %s с id = %s",
                subscriber.getProfile().getSurname(),
                subscriber.getId(),
                subscribed.getProfile().getSurname(),
                subscribed.getId()
        );
    }

    public String unsubscribe(UUID subscriber_id, UUID subscribed_id) {
        User subscriber = userRepository.findById(subscriber_id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        User subscribed = userRepository.findById(subscribed_id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        subscriptionRepository.delete(subscriptionRepository.findBySubscriberUserIdAndSubscribedUserId(subscriber_id, subscribed_id));
        return String.format(
                "Пользователь %s с id = %s успешно отписан на пользователя %s с id = %s",
                subscriber.getProfile().getSurname(),
                subscriber.getId(),
                subscribed.getProfile().getSurname(),
                subscribed.getId()
        );
    }

    public String addSkill(UUID user_id, Integer skill_id) {
        User user = userRepository.findById(user_id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        Skill skill = skillRepository.findById(skill_id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        hardSkillsRepository.save(
                new HardSkills(user_id, skill_id)
        );
        return String.format(
                "Пользователю %s с id = %s успешно добавлена компетенция %s с id = %s",
                user.getProfile().getSurname(),
                user.getId(),
                skill.getName(),
                skill.getId()
        );
    }

    public String removeSkill(UUID user_id, Integer skill_id) {
        User user = userRepository.findById(user_id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        Skill skill = skillRepository.findById(skill_id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        HardSkills hardSkills = hardSkillsRepository.findByUserIdAndSkillId(user_id, skill_id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        hardSkillsRepository.delete(hardSkills);
        return String.format(
                "У пользователя %s с id = %s успешно удалена компетенция %s с id = %s",
                user.getProfile().getSurname(),
                user.getId(),
                skill.getName(),
                skill.getId()
        );
    }
}
