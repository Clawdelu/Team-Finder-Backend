package com.theinnovationtrio.TeamFinderAPI.technologyStack;

import java.util.List;
import java.util.UUID;

public interface ITechnologyStackService {

    TechnologyStack createTechnologyStack(TechnologyStackDto technologyStackDto);
    List<TechnologyStack> createListOfTechnologyStacks(List<TechnologyStackDto> technologyStackDtoList);

    TechnologyStack getTechnologyStackById(UUID technologyStackId);

    List<TechnologyStack> getAllTechnologyStacks();

    TechnologyStack updateTechnologyStack(UUID technologyStackId, TechnologyStackDto technologyStackDto);

    void deleteTechnologyStackById(UUID technologyStackId);

    void deleteListOfTechnologyStacks(List<TechnologyStack> technologyStackList);
}
