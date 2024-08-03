package com.theinnovationtrio.TeamFinderAPI.technologyStack;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TechnologyStackService implements ITechnologyStackService{

    private final TechnologyStackRepository technologyStackRepository;
    @Override
    public TechnologyStack createTechnologyStack(TechnologyStackDto technologyStackDto) {
        TechnologyStack technologyStack = TechnologyStack.builder()
                .id(UUID.randomUUID())
                .technologyName(technologyStackDto.getTechnologyName())
                .build();
        return technologyStackRepository.save(technologyStack);
    }

    @Override
    public List<TechnologyStack>  createListOfTechnologyStacks(List<TechnologyStackDto> technologyStackDtoList) {

        List<TechnologyStack> technologyStacks = new ArrayList<>();

        technologyStackDtoList.forEach(technologyStackDto -> technologyStacks.add(createTechnologyStack(technologyStackDto)));

        return technologyStacks;
    }

    @Override
    public TechnologyStack getTechnologyStackById(UUID technologyStackId) {
        return technologyStackRepository.findById(technologyStackId)
                .orElseThrow(() -> new EntityNotFoundException("Technology Stack not found"));
    }

    @Override
    public List<TechnologyStack> getAllTechnologyStacks() {
        return technologyStackRepository.findAll();
    }

    @Override
    public TechnologyStack updateTechnologyStack(UUID technologyStackId, TechnologyStackDto technologyStackDto) {

        TechnologyStack technologyStackToUpdate = getTechnologyStackById(technologyStackId);
        technologyStackToUpdate.setTechnologyName(technologyStackDto.getTechnologyName());

        return technologyStackRepository.save(technologyStackToUpdate);
    }

    @Override
    public void deleteTechnologyStackById(UUID technologyStackId) {
        //TODO : trebuie sa mergi la Project si sa stergi din lista respectiva; de asemenea cand se sterge un Project sa stergem si techStack ??
        technologyStackRepository.deleteById(technologyStackId);
    }

    @Override
    public void deleteListOfTechnologyStacks(List<TechnologyStack> technologyStackList) {
        technologyStackList.forEach(technologyStack -> deleteTechnologyStackById(technologyStack.getId()));
    }
}
