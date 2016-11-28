package de.lmu.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import de.lmu.domain.Appointment;

public interface AppointmentRepository extends JpaRepository<Appointment, Long> {
	Appointment findById(Long id);

	Appointment deleteById(Long id);
}
