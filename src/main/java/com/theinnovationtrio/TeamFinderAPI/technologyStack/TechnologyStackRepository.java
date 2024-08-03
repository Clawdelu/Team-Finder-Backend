package com.theinnovationtrio.TeamFinderAPI.technologyStack;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface TechnologyStackRepository extends JpaRepository<TechnologyStack, UUID> {
}
