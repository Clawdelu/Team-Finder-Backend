package com.theinnovationtrio.TeamFinderAPI.user_skill;

import com.theinnovationtrio.TeamFinderAPI.enums.Experience;
import com.theinnovationtrio.TeamFinderAPI.enums.Level;
import com.theinnovationtrio.TeamFinderAPI.skill.Skill;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.*;

import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class User_Skill {
    @Id
    private UUID id;
    @ManyToOne
    private Skill skill;
    private Level level;
    private Experience experience;

}
