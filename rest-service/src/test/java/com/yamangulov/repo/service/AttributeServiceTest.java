package com.yamangulov.repo.service;

import com.yamangulov.repo.entity.City;
import com.yamangulov.repo.entity.Gender;
import com.yamangulov.repo.entity.Skill;
import com.yamangulov.repo.repository.CityRepository;
import com.yamangulov.repo.repository.GenderRepository;
import com.yamangulov.repo.repository.SkillRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
class AttributeServiceTest {

    @Mock
    private CityRepository cityRepository;
    @Mock
    private GenderRepository genderRepository;
    @Mock
    private SkillRepository skillRepository;
    @InjectMocks
    private AttributeService attributeService;

    @Test
    void addCity() {
        City city = new City(1,"Moscow");
        Mockito.when(cityRepository.save(Mockito.any(City.class))).thenReturn(city);
        String response = attributeService.addCity("Moscow");
        assertEquals(
                String.format(
                        "Город %s добавлен в БД",
                        city.getName()
                ),
                response
        );
    }

    @Test
    void addGender() {
        Gender gender = new Gender("male");
        Mockito.when(genderRepository.save(Mockito.any(Gender.class))).thenReturn(gender);
        assertEquals(
                String.format(
                        "Пол %s добавлен в БД",
                        gender.getName()
                ),
                attributeService.addGender("male")
        );
    }

    @Test
    void addSkill() {
        Skill skill = new Skill("java");
        Mockito.when(skillRepository.save(Mockito.any(Skill.class))).thenReturn(skill);
        assertEquals(
                String.format(
                        "Компетенция %s добавлена в БД",
                        skill.getName()
                ),
                attributeService.addSkill("java")
        );
    }
}