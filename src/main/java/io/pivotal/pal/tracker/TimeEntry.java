package io.pivotal.pal.tracker;

import java.time.LocalDate;
import java.util.concurrent.atomic.AtomicLong;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TimeEntry {
	private long id;
	private long projectId;
	private long userId;
	private LocalDate date;
	private int hours;

	public TimeEntry(long projectId, long userId, LocalDate parse, int hours) {
		this.projectId = projectId;
		this.userId = userId;
		this.date = parse;
		this.hours = hours;
	}
}
