package io.pivotal.pal.tracker;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

import lombok.SneakyThrows;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TimeEntryController {

	private TimeEntryRepository timeEntryRepository;

	public TimeEntryController(TimeEntryRepository timeEntryRepository) {
		this.timeEntryRepository = timeEntryRepository;
	}

	@SneakyThrows
	@PostMapping("/time-entries")
	public ResponseEntity create(@RequestBody TimeEntry timeEntryToCreate) {
		TimeEntry timeEntry = timeEntryRepository.create(timeEntryToCreate);
		return ResponseEntity.created(new URI("/time-entry/" + timeEntry.getId())).body(timeEntry);
	}

	@GetMapping(path = "/time-entries/{id}")
	public ResponseEntity<TimeEntry> read(@PathVariable("id") long nonExistentTimeEntryId) {
		TimeEntry timeEntry = timeEntryRepository.find(nonExistentTimeEntryId);
		if(null != timeEntry) {
			return ResponseEntity.ok(timeEntry);
		} else {
			return ResponseEntity.notFound().build();
		}
	}

	@GetMapping("/time-entries")
	public ResponseEntity<List<TimeEntry>> list() {
		return ResponseEntity.ok(timeEntryRepository.list());
	}

	@PutMapping(path = "/time-entries/{id}")
	public ResponseEntity update(@PathVariable("id") long timeEntryId, @RequestBody TimeEntry expected) {
		TimeEntry timeEntry = timeEntryRepository.update(timeEntryId, expected);
		if(null != timeEntry) {
			return ResponseEntity.ok(timeEntry);
		} else {
			return ResponseEntity.notFound().build();
		}
	}

	@DeleteMapping(path = "/time-entries/{id}")
	public ResponseEntity delete(@PathVariable("id") long timeEntryId) {
		timeEntryRepository.delete(timeEntryId);
		return ResponseEntity.noContent().build();
	}
}
