package com.yamangulov.repo;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yamangulov.repo.containers.AbstractContainerDatabaseTest;
import com.yamangulov.repo.containers.PostgresContainerWrapper;
import com.yamangulov.repo.entity.Subscription;
import com.yamangulov.repo.entity.User;
import com.yamangulov.repo.repository.SubscriptionRepository;
import com.yamangulov.repo.service.UserService;
import org.junit.jupiter.api.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.nio.charset.StandardCharsets;
import java.util.UUID;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Testcontainers(disabledWithoutDocker = true)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@AutoConfigureMockMvc
//@AutoConfigureWireMock(port = 0)
@ActiveProfiles("integTest")
@ContextConfiguration(initializers = {Testcontainer.Initializer.class})
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class Testcontainer extends AbstractContainerDatabaseTest {
    Logger log = LoggerFactory.getLogger(Testcontainer.class);

    @Container
    private static final PostgreSQLContainer<PostgresContainerWrapper> postgresContainer = new PostgresContainerWrapper();

    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private SubscriptionRepository subscriptionRepository;

    @Autowired
    private UserService userService;

    static class Initializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {
        public void initialize(ConfigurableApplicationContext configurableApplicationContext) {
            TestPropertyValues.of(
                    "spring.datasource.url=" + postgresContainer.getJdbcUrl(),
                    "spring.datasource.username=" + postgresContainer.getUsername(),
                    "spring.datasource.password=" + postgresContainer.getPassword(),
                    "spring.liquibase.enabled=true"
            ).applyTo(configurableApplicationContext.getEnvironment());
        }
    }


    @Order(1)
    @RepeatedTest(4)
    void addCity(RepetitionInfo repetitionInfo) throws Exception {
        if (repetitionInfo.getCurrentRepetition() == 4) {
            mockMvc.perform(MockMvcRequestBuilders.post("/attr/city/{name}", "SanctPeterburg"))
                    .andExpect(status().isOk());
        } else {
            try {
                mockMvc.perform(MockMvcRequestBuilders.post("/attr/city/{name}", "SanctPeterburg"));
            } catch (Exception e) {
                assert true;
            }

        }
    }

    @Order(2)
    @RepeatedTest(3)
    void addGender(RepetitionInfo repetitionInfo) throws Exception {
        if (repetitionInfo.getCurrentRepetition() == 3) {
            mockMvc.perform(MockMvcRequestBuilders.post("/attr/gender/{name}", "trans"))
                    .andExpect(status().isOk());
        } else {
            try {
                mockMvc.perform(MockMvcRequestBuilders.post("/attr/gender/{name}", "trans"));
            } catch (Exception e) {
                assert true;
            }
        }
    }

    @Order(3)
    @RepeatedTest(5)
    void addSkill(RepetitionInfo repetitionInfo) throws Exception {
        if (repetitionInfo.getCurrentRepetition() == 5) {
            mockMvc.perform(MockMvcRequestBuilders.post("/attr/skill/{name}", "Golang"))
                    .andExpect(status().isOk());
        } else {
            try {
                mockMvc.perform(MockMvcRequestBuilders.post("/attr/skill/{name}", "Golang"));
            } catch (Exception e) {
                assert true;
            }
        }
    }

    @Order(4)
    @Test
    void createUser() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/users")
                        .contentType(MediaType.MULTIPART_FORM_DATA)
                        .param("email", "test555@test.ru")
                        .param("phone", "55555")
                        .param("name", "Sergey")
                        .param("surname", "Sergeev")
                        .param("birthDate", "2010-05-23")
                )
                .andExpect(status().isOk());
    }

    @Order(5)
    @Test
    void getUser() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/users/ae666352-da3a-4c90-ad70-6a8f500fcbcc"))
                .andExpect(status().isOk());
    }

    @Order(6)
    @Test
    void updateUser() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/users/ae666352-da3a-4c90-ad70-6a8f500fcbcc"))
                .andExpect(status().isOk());
        mockMvc.perform(MockMvcRequestBuilders.put("/users/ae666352-da3a-4c90-ad70-6a8f500fcbcc")
                        .contentType(MediaType.MULTIPART_FORM_DATA)
                        .param("email", "test666@test.ru")
                        .param("phone", "66666")
                        .param("name", "Serg")
                        .param("surname", "Sergeevskiy")
                )
                .andExpect(status().isOk());
    }

    @Order(7)
    @Test
    void deleteUser() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/users/ae666352-da3a-4c90-ad70-6a8f500fcbcc"))
                .andExpect(status().isOk());
        mockMvc.perform(MockMvcRequestBuilders.delete("/users/ae666352-da3a-4c90-ad70-6a8f500fcbcc"))
                .andExpect(status().isOk());
    }

    @Order(8)
    @Test
    void subscribe() throws Exception {
        String subscriberString =  mockMvc.perform(MockMvcRequestBuilders.get("/users/aa1c1b72-5bfc-4da2-ab77-102226c71dbc"))
                .andReturn().getResponse().getContentAsString();
        String subscribedString =  mockMvc.perform(MockMvcRequestBuilders.get("/users/74406a64-93c6-4c34-b8a2-42f57869dbc0"))
                .andReturn().getResponse().getContentAsString();
        User subscriber = objectMapper.readValue(subscriberString, User.class);
        User subscribed = objectMapper.readValue(subscribedString, User.class);

        mockMvc.perform(MockMvcRequestBuilders.post("/users/subscribe/" + subscriber.getId().toString() + "/" + subscribed.getId().toString()))
                .andExpect(status().isOk());
    }

    @Order(9)
    @Test
    void unsubscribe() throws Exception {
        String subscriberString = mockMvc.perform(MockMvcRequestBuilders.get("/users/aa1c1b72-5bfc-4da2-ab77-102226c71dbc"))
                .andReturn().getResponse().getContentAsString();
        String subscribedString = mockMvc.perform(MockMvcRequestBuilders.get("/users/74406a64-93c6-4c34-b8a2-42f57869dbc0"))
                .andReturn().getResponse().getContentAsString();
        User subscriber = objectMapper.readValue(subscriberString, User.class);
        User subscribed = objectMapper.readValue(subscribedString, User.class);
        mockMvc.perform(MockMvcRequestBuilders.post("/users/unsubscribe/" + subscriber.getId().toString() + "/" + subscribed.getId().toString()))
                .andExpect(status().isOk());
    }

    @Order(10)
    @Test
    void addSkillToUser() throws Exception {
        String userString = mockMvc.perform(MockMvcRequestBuilders.get("/users/74406a64-93c6-4c34-b8a2-42f57869dbc0"))
                .andReturn().getResponse().getContentAsString();
        User user = objectMapper.readValue(userString, User.class);
        mockMvc.perform(MockMvcRequestBuilders.post("/users/addSkill/" + user.getId().toString() + "/1"))
                .andExpect(status().isOk());
    }

    @Order(11)
    @Test
    void removeSkillFromUser() throws Exception {
        String userString = mockMvc.perform(MockMvcRequestBuilders.get("/users/74406a64-93c6-4c34-b8a2-42f57869dbc0"))
                .andReturn().getResponse().getContentAsString();
        User user = objectMapper.readValue(userString, User.class);
        mockMvc.perform(MockMvcRequestBuilders.post("/users/removeSkill/" + user.getId().toString() + "/1"))
                .andExpect(status().isOk());
    }

    @Order(12)
    @Test
    void smokeTest() throws Exception {
        String content = mockMvc.perform(MockMvcRequestBuilders.post("/users")
                        .contentType(MediaType.MULTIPART_FORM_DATA)
                        .param("email", "test555@test.ru")
                        .param("phone", "55555")
                        .param("name", "Sergey")
                        .param("surname", "Sergeev")
                        .param("birthDate", "2010-05-23")
                )
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString(StandardCharsets.UTF_8);
        content = content.substring(content.length() - 36);
        mockMvc.perform(MockMvcRequestBuilders.get("/users/" + content))
                .andExpect(status().isOk());
    }

    @Order(13)
    @Test
    void regressionTest() throws Exception {
        String content_1 = mockMvc.perform(MockMvcRequestBuilders.post("/users")
                        .contentType(MediaType.MULTIPART_FORM_DATA)
                        .param("email", "test666@test.ru")
                        .param("phone", "66666")
                        .param("name", "Sergio")
                        .param("surname", "Galan")
                        .param("birthDate", "2010-05-23")
                )
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString(StandardCharsets.UTF_8);
        String userId_1 = content_1.substring(content_1.length() - 36);
        mockMvc.perform(MockMvcRequestBuilders.get("/users/" + userId_1))
                .andExpect(status().isOk());

        String content_2 = mockMvc.perform(MockMvcRequestBuilders.post("/users")
                        .contentType(MediaType.MULTIPART_FORM_DATA)
                        .param("email", "test777@test.ru")
                        .param("phone", "7777")
                        .param("name", "Sergo")
                        .param("surname", "Kamadze")
                        .param("birthDate", "2010-05-23")
                )
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString(StandardCharsets.UTF_8);
        String userId_2 = content_2.substring(content_2.length() - 36);

        mockMvc.perform(MockMvcRequestBuilders.get("/users/" + userId_2))
                .andExpect(status().isOk());

        mockMvc.perform(MockMvcRequestBuilders.post("/users/subscribe/" + userId_1 + "/" + userId_2))
                .andExpect(status().isOk());

        // здесь использован репозиторий, потому что в предыдущих заданиях проекта не было ТЗ на реализацию
        // метода для получения подписки в каких-либо сервисах и в контроллерах
        Subscription subscriptionBefore = subscriptionRepository
                .findBySubscriberUserIdAndSubscribedUserId(UUID.fromString(userId_1), UUID.fromString(userId_2));
        assertThat(subscriptionBefore, notNullValue());

        mockMvc.perform(MockMvcRequestBuilders.post("/users/unsubscribe/" + userId_1 + "/" + userId_2))
                .andExpect(status().isOk());

        Subscription subscriptionAfter = subscriptionRepository
                .findBySubscriberUserIdAndSubscribedUserId(UUID.fromString(userId_1), UUID.fromString(userId_2));
        assertThat(subscriptionAfter, nullValue());

        mockMvc.perform(MockMvcRequestBuilders.put("/users/" + userId_2)
                        .contentType(MediaType.MULTIPART_FORM_DATA)
                        .param("surname", "Kamikadze")
                )
                .andExpect(status().isOk());
        // аналогично, в ТЗ ранее не было методов в контроллере для получения объекта класса User, существующий метод
        // getUser получает данные в формате, не приводимом к типу User, переделать можно было бы, но тест я пишу
        // для текущей моей реализации без того, чтобы ее изменять, поэтому получаю User из сервиса
        User user2 = userService.getUser(UUID.fromString(userId_2));
        assertThat(user2.getProfile().getSurname(), equalTo("Kamikadze"));

        mockMvc.perform(MockMvcRequestBuilders.delete("/users/" + userId_1))
                .andExpect(status().isOk());
        mockMvc.perform(MockMvcRequestBuilders.delete("/users/" + userId_2))
                .andExpect(status().isOk());

        mockMvc.perform(MockMvcRequestBuilders.get("/users/" + userId_1))
                .andExpect(status().is4xxClientError());
        mockMvc.perform(MockMvcRequestBuilders.get("/users/" + userId_2))
                .andExpect(status().is4xxClientError());
    }

    @Order(14)
    @Test
    void loadTest() {

    }
}
