package com.yamangulov.repo.service;

import com.yamangulov.repo.entity.City;
import com.yamangulov.repo.entity.Gender;
import com.yamangulov.repo.entity.Skill;
import com.yamangulov.repo.repository.CityRepository;
import com.yamangulov.repo.repository.GenderRepository;
import com.yamangulov.repo.repository.SkillRepository;
import org.springframework.stereotype.Service;

@Service
public class AttributeService {
    private final CityRepository cityRepository;
    private final GenderRepository genderRepository;
    private final SkillRepository skillRepository;


    public AttributeService(CityRepository cityRepository, GenderRepository genderRepository, SkillRepository skillRepository) {
        this.cityRepository = cityRepository;
        this.genderRepository = genderRepository;
        this.skillRepository = skillRepository;
    }

    public String addCity(String name) {
        cityRepository.save(
                new City(name)
        );
        return String.format(
                "Город %s добавлен в БД",
                name
        );
    }

    public String addGender(String name) {
        genderRepository.save(
                new Gender(name)
        );
        return String.format(
                "Пол %s добавлен в БД",
                name
        );
    }

    public String addSkill(String name) {
        skillRepository.save(
                new Skill(name)
        );
        return String.format(
                "Компетенция %s добавлена в БД",
                name
        );
    }
}
