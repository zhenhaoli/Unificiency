package de.lmu.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import de.lmu.domain.Appointment;
import de.lmu.repository.AppointmentRepository;

@RestController
public class AppointmentController {

	@Autowired
	AppointmentRepository appointmentRepository;

	/** creates an appointment */
	@RequestMapping(value = "/appointments", method = RequestMethod.POST)
	public ResponseEntity<Void> createAppointment(@RequestBody Appointment appointment,
			UriComponentsBuilder ucBuilder) {

		appointmentRepository.save(appointment);

		HttpHeaders headers = new HttpHeaders();
		headers.setLocation(ucBuilder.path("/user/{id}").buildAndExpand(appointment.getId()).toUri());
		return new ResponseEntity<Void>(headers, HttpStatus.CREATED);
	}

	/** retrieve all appointments */
	@RequestMapping(value = "/appointments", method = RequestMethod.GET)
	public ResponseEntity<List<Appointment>> listAppointments() {
		List<Appointment> appointments = appointmentRepository.findAll();
		if (appointments.isEmpty()) {
			return new ResponseEntity<List<Appointment>>(HttpStatus.NO_CONTENT);
		}
		return new ResponseEntity<List<Appointment>>(appointments, HttpStatus.OK);
	}

	/** retrieve an appointment by id */
	@RequestMapping(value = "/appointments/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Appointment> getAppointment(@PathVariable("id") Long id) {

		Appointment appointment = appointmentRepository.findById(id);
		if (appointment == null) {
			return new ResponseEntity<Appointment>(HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<Appointment>(appointment, HttpStatus.OK);
	}

	/** updates an appointment by id */
	@RequestMapping(value = "/appointments/{id}", method = RequestMethod.PUT)
	public ResponseEntity<Appointment> updateAppointment(@PathVariable("id") long id,
			@RequestBody Appointment appointment) {
		System.out.println("Updating User " + id);

		Appointment currentAppointment = appointmentRepository.findById(id);

		if (currentAppointment == null) {
			return new ResponseEntity<Appointment>(HttpStatus.NOT_FOUND);
		}

		appointmentRepository.save(appointment);
		return new ResponseEntity<Appointment>(appointment, HttpStatus.OK);
	}

	/** deletes an appointment */
	@RequestMapping(value = "/appointments/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<Appointment> deleteAppointment(@PathVariable("id") long id) {

		Appointment appointment = appointmentRepository.findById(id);
		if (appointment == null) {
			return new ResponseEntity<Appointment>(HttpStatus.NOT_FOUND);
		}

		appointmentRepository.deleteById(id);
		return new ResponseEntity<Appointment>(HttpStatus.NO_CONTENT);
	}

	/** deletes all appointments */
	@RequestMapping(value = "/appointments", method = RequestMethod.DELETE)
	public ResponseEntity<List<Appointment>> deleteAllAppointments() {
		appointmentRepository.deleteAll();
		return new ResponseEntity<List<Appointment>>(HttpStatus.NO_CONTENT);
	}

}
