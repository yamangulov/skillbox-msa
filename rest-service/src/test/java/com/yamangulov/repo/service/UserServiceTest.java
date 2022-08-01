package com.yamangulov.repo.service;

import com.yamangulov.repo.entity.*;
import com.yamangulov.repo.repository.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private ContactRepository contactRepository;
    @Mock
    private ProfileRepository profileRepository;
    @Mock
    private SubscriptionRepository subscriptionRepository;
    @Mock
    private SkillRepository skillRepository;
    @Mock
    private HardSkillsRepository hardSkillsRepository;
    @InjectMocks
    private UserService userService;

    private User user;
    private Contact contact;
    private Profile profile;
    private UUID uuid;
    private UUID subsriber_user_id;
    private UUID subsribed_user_id;
    private User subscriber;
    private User subscribed;

    private Skill skill;
    private HardSkills hardSkill;

    @BeforeEach
    void setUp() {
        uuid = UUID.randomUUID();
        UUID contactUuid = UUID.randomUUID();
        UUID profileUuid = UUID.randomUUID();
        contact = new Contact(contactUuid, "test@test.ru", "111");
        profile = new Profile(profileUuid, "Ivan", "Ivanov", "2010-05-23", contact);
        user = new User(uuid, profile);
        subsriber_user_id = UUID.randomUUID();
        subsribed_user_id = UUID.randomUUID();
        UUID subscriberContactUuid = UUID.randomUUID();
        UUID subscriberProfileUuid = UUID.randomUUID();
        UUID subscribedContactUuid = UUID.randomUUID();
        UUID subscribedProfileUuid = UUID.randomUUID();
        Contact subscriberContact = new Contact(subscriberContactUuid, "test222@test.ru", "222");
        Profile subscriberProfile = new Profile(subscriberProfileUuid, "Ivan", "Petrov", "2010-05-23", subscriberContact);
        Contact subscribedContact = new Contact(subscribedContactUuid, "test333@test.ru", "333");
        Profile subscribedProfile = new Profile(subscribedProfileUuid, "Ivan", "Sidorov", "2010-05-23", subscribedContact);
        subscriber = new User(subsriber_user_id, subscriberProfile);
        subscribed = new User(subsribed_user_id, subscribedProfile);
        skill = new Skill(1, "java");
        hardSkill = new HardSkills(user.getId(),skill.getId());
    }

    @Test
    void creatUser() {
        Map<String, String> requestParams = new HashMap<>();
        requestParams.put("email", "test@test.ru");
        requestParams.put("phone", "111");
        requestParams.put("name", "Ivan");
        requestParams.put("surname", "Ivanov");
        requestParams.put("birthDate", "2010-05-23");
        Mockito.when(contactRepository.save(Mockito.any(Contact.class))).thenReturn(contact);
        Mockito.when(profileRepository.save(Mockito.any(Profile.class))).thenReturn(profile);
        Mockito.when(userRepository.save(Mockito.any(User.class))).thenReturn(user);
        String response = userService.creatUser(requestParams);
        assertEquals("Пользователь Ivanov добавлен в БД с id = " + user.getId(), response);
    }

    @Test
    void getUser() {
        Mockito.when(userRepository.findById(Mockito.any(UUID.class))).thenReturn(Optional.ofNullable(user));
        UUID id = userService.getUser(uuid).getId();
        assertEquals(uuid, id);
    }

    @Test
    void updateUser() {
        Map<String, String> requestParams = new HashMap<>();
        requestParams.put("email", "test222@test.ru");
        requestParams.put("phone", "222");
        requestParams.put("surname", "Ivanovskiy");
        Mockito.when(contactRepository.save(Mockito.any(Contact.class))).thenReturn(contact);
        Mockito.when(profileRepository.save(Mockito.any(Profile.class))).thenReturn(profile);
        Mockito.when(userRepository.findById(Mockito.any(UUID.class))).thenReturn(Optional.ofNullable(user));
        String response = userService.updateUser(requestParams, uuid);
        assertEquals(
                String.format(
                        "Данные пользователя %s с id = %s успешно обновлены в БД",
                        user.getProfile().getSurname(),
                        user.getId()
                ),
                response
        );
    }

    @Test
    void deleteUser() {
        Mockito.when(userRepository.findById(Mockito.any(UUID.class))).thenReturn(Optional.ofNullable(user));
        User deletedUser = userService.getUser(uuid);
        String response = userService.deleteUser(uuid);
        assertEquals(
                String.format(
                        "Пользователя %s с id = %s успешно удален из БД",
                        deletedUser.getProfile().getSurname(),
                        deletedUser.getId()
                ),
                response
        );
    }

    @Test
    void subscribe() {
        Mockito.when(userRepository.findById(Mockito.any(UUID.class))).thenAnswer(invocation -> {
            if(invocation.getArgument(0).equals(subsriber_user_id)) {
                return Optional.of(subscriber);
            } else if (invocation.getArgument(0).equals(subsribed_user_id)) {
                return Optional.of(subscribed);
            } else {
                throw new IllegalArgumentException("Один или оба пользователя не найдены");
            }
        });
        String response = userService.subscribe(subsriber_user_id, subsribed_user_id);
        assertEquals(
                String.format(
                        "Пользователь %s с id = %s успешно подписан на пользователя %s с id = %s",
                        subscriber.getProfile().getSurname(),
                        subscriber.getId(),
                        subscribed.getProfile().getSurname(),
                        subscribed.getId()
                ),
                response
        );
    }

    @Test
    void unsubscribe() {
        Mockito.when(userRepository.findById(Mockito.any(UUID.class))).thenAnswer(invocation -> {
            if(invocation.getArgument(0).equals(subsriber_user_id)) {
                return Optional.of(subscriber);
            } else if (invocation.getArgument(0).equals(subsribed_user_id)) {
                return Optional.of(subscribed);
            } else {
                throw new IllegalArgumentException("Один или оба пользователя не найдены");
            }
        });
        String response = userService.unsubscribe(subsriber_user_id, subsribed_user_id);
        assertEquals(
                String.format(
                        "Пользователь %s с id = %s успешно отписан на пользователя %s с id = %s",
                        subscriber.getProfile().getSurname(),
                        subscriber.getId(),
                        subscribed.getProfile().getSurname(),
                        subscribed.getId()
                ),
                response
        );
    }

    @Test
    void addSkill() {
        Mockito.when(userRepository.findById(Mockito.any(UUID.class))).thenReturn(Optional.ofNullable(user));
        Mockito.when(skillRepository.findById(Mockito.any(Integer.class))).thenReturn(Optional.ofNullable(skill));
        String response = userService.addSkill(user.getId(), skill.getId());
        assertEquals(
                String.format(
                        "Пользователю %s с id = %s успешно добавлена компетенция %s с id = %s",
                        user.getProfile().getSurname(),
                        user.getId(),
                        skill.getName(),
                        skill.getId()
                ),
                response
        );
    }

    @Test
    void removeSkill() {
        Mockito.when(userRepository.findById(Mockito.any(UUID.class))).thenReturn(Optional.ofNullable(user));
        Mockito.when(skillRepository.findById(Mockito.any(Integer.class))).thenReturn(Optional.ofNullable(skill));
        Mockito.when(hardSkillsRepository.findByUserIdAndSkillId(user.getId(), skill.getId())).thenReturn(Optional.ofNullable(hardSkill));
        String response = userService.removeSkill(user.getId(), skill.getId());
        assertEquals(
                String.format(
                        "У пользователя %s с id = %s успешно удалена компетенция %s с id = %s",
                        user.getProfile().getSurname(),
                        user.getId(),
                        skill.getName(),
                        skill.getId()
                ),
                response
        );
    }
}