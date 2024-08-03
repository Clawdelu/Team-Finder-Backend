package com.theinnovationtrio.TeamFinderAPI.user_skill;

import com.theinnovationtrio.TeamFinderAPI.skill.ISkillService;
import com.theinnovationtrio.TeamFinderAPI.skill.Skill;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserSkillService implements IUserSkillService {

    private final UserSkillRepository userSkillRepository;
    private final ISkillService skillService;

    @Override
    public User_Skill createUserSkill(UserSkillDto userSkillDto, UUID skillId) {
        User_Skill userSkill = User_Skill.builder()
                .id(UUID.randomUUID())
                .skill(skillService.getSkillById(skillId))
                .level(userSkillDto.getLevel())
                .experience(userSkillDto.getExperience())
                .build();
        return userSkillRepository.save(userSkill);
    }

    @Override
    public User_Skill getUserSkillById(UUID userSkillId) {
        return userSkillRepository.findById(userSkillId)
                .orElseThrow(() -> new EntityNotFoundException("User_Skill not found!"));

    }

    @Override
    public List<User_Skill> getAllUsersSkills() {
        return userSkillRepository.findAll();
    }

    @Override
    public User_Skill updateUserSkill(UUID userSkillId, UserSkillDto userSkillDto) {
        User_Skill userSkill = getUserSkillById(userSkillId);
        userSkill.setLevel(userSkillDto.getLevel());
        userSkill.setExperience(userSkillDto.getExperience());
        return userSkillRepository.save(userSkill);
    }

    @Override
    public List<User_Skill> getAllUserSkillsBySkill(Skill skill) {
        return userSkillRepository.getAllBySkill(skill);
    }


    @Override
    public void deleteUserSkillById(UUID userSkillId) {
        userSkillRepository.deleteById(userSkillId);
    }

}
