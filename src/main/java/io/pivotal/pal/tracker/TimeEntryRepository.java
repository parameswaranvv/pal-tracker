package io.pivotal.pal.tracker;

import java.util.List;

public interface TimeEntryRepository {
	TimeEntry create(TimeEntry any);

	TimeEntry find(long nonExistentTimeEntryId);

	List list();

	TimeEntry update(long eq, TimeEntry any);

	void delete(long timeEntryId);
}
