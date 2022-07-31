package com.yamangulov.repo.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "hard_skills")
@Getter
@Setter
@ToString
@RequiredArgsConstructor
public class HardSkills {
    @Id
    @GeneratedValue
    private UUID id;

    @Column(name = "user_id")
    private UUID userId;

    @Column(name = "skill_id")
    private Integer skillId;

    public HardSkills(UUID userId, Integer skillId) {
        this.userId = userId;
        this.skillId = skillId;
    }
}
