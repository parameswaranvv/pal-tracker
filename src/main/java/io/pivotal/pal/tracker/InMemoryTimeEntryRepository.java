package io.pivotal.pal.tracker;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

import org.springframework.stereotype.Repository;

public class InMemoryTimeEntryRepository implements TimeEntryRepository {

	private Map<Long, TimeEntry> dataStore = new HashMap<>();

	private AtomicLong sequence = new AtomicLong(1L);

	@Override
	public TimeEntry create(TimeEntry timeEntry) {
		long id = getNextId();
		timeEntry.setId(id);
		dataStore.put(id, timeEntry);
		return dataStore.get(id);
	}

	@Override
	public TimeEntry find(long timeEntryId) {
		return dataStore.get(timeEntryId);
	}

	@Override
	public List list() {
		return dataStore.entrySet().stream().map(t -> t.getValue()).collect(Collectors.toList());
	}

	@Override
	public TimeEntry update(long id, TimeEntry timeEntry) {
		Optional<TimeEntry> optionalTimeEntry = Optional.ofNullable(dataStore.get(id));
		if (!optionalTimeEntry.isEmpty()) {
			TimeEntry timeEntryToSave = optionalTimeEntry.get();
			timeEntryToSave.setUserId(timeEntry.getUserId());
			timeEntryToSave.setProjectId(timeEntry.getProjectId());
			timeEntryToSave.setHours(timeEntry.getHours());
			timeEntryToSave.setDate(timeEntry.getDate());
			dataStore.put(id, timeEntryToSave);
			return dataStore.get(id);
		}
		return null;
	}

	@Override
	public void delete(long timeEntryId) {
		dataStore.remove(timeEntryId);
	}

	private long getNextId() {
		return sequence.getAndIncrement();
	}
}
