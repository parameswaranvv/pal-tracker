package io.pivotal.pal.tracker;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.util.CollectionUtils;


public class JdbcTimeEntryRepository implements TimeEntryRepository {

	private JdbcTemplate jdbcTemplate;

	public JdbcTimeEntryRepository(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}

	@Override
	public TimeEntry create(TimeEntry timeEntry) {
		PreparedStatementCreator preparedStatementCreator = conn -> {
			PreparedStatement preparedStatement = conn.prepareStatement(
					"INSERT INTO time_entries(project_id, user_id, date, hours) VALUES(?, ?, ?, ?)",
					Statement.RETURN_GENERATED_KEYS);
			preparedStatement.setLong(1, timeEntry.getProjectId());
			preparedStatement.setLong(2, timeEntry.getUserId());
			preparedStatement.setDate(3, Date.valueOf(timeEntry.getDate()));
			preparedStatement.setInt(4, timeEntry.getHours());
			return preparedStatement;
		};
		KeyHolder generatedKeyHolder = new GeneratedKeyHolder();
		jdbcTemplate.update(preparedStatementCreator, generatedKeyHolder);
		timeEntry.setId(generatedKeyHolder.getKey().longValue());
		return timeEntry;
	}

	@Override
	public TimeEntry find(long nonExistentTimeEntryId) {
		String sql = "SELECT * FROM TIME_ENTRIES where id = ?";
		List<TimeEntry> entries = jdbcTemplate.query(sql, new Object[] { nonExistentTimeEntryId }, (rs, rowNum) -> {
			TimeEntry entry = new TimeEntry();
			entry.setId(rs.getLong(1));
			entry.setProjectId(rs.getLong(2));
			entry.setUserId(rs.getLong(3));
			entry.setDate(rs.getDate(4).toLocalDate());
			entry.setHours(rs.getInt(5));
			return entry;
		});
		if (CollectionUtils.isEmpty(entries)) return null;
		return entries.get(0);
	}

	@Override
	public List list() {
		String sql = "SELECT * FROM TIME_ENTRIES";
		return jdbcTemplate.query(sql, (rs, rowNum) -> {
			TimeEntry entry = new TimeEntry();
			entry.setId(rs.getLong(1));
			entry.setProjectId(rs.getLong(2));
			entry.setUserId(rs.getLong(3));
			entry.setDate(rs.getDate(4).toLocalDate());
			entry.setHours(rs.getInt(5));
			return entry;
		});
	}

	@Override
	public TimeEntry update(long id, TimeEntry timeEntry) {
		jdbcTemplate.update("UPDATE time_entries " +
						"SET project_id = ?, user_id = ?, date = ?,  hours = ? " +
						"WHERE id = ?",
				timeEntry.getProjectId(),
				timeEntry.getUserId(),
				Date.valueOf(timeEntry.getDate()),
				timeEntry.getHours(),
				id);

		return find(id);
	}

	@Override
	public void delete(long timeEntryId) {
		jdbcTemplate.update("DELETE FROM time_entries WHERE id = ?", timeEntryId);
	}

}
