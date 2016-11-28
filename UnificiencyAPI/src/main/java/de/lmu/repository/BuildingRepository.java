package de.lmu.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import de.lmu.domain.Building;

public interface BuildingRepository extends JpaRepository<Building, Long> {

}
