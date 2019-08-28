package io.pivotal.pal.tracker;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import javax.sql.DataSource;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.util.List;

public class JdbcTimeEntryRepository implements TimeEntryRepository {

    private JdbcTemplate jdbctemplate;

    public JdbcTimeEntryRepository(DataSource datasource)
    {
        this.jdbctemplate = new JdbcTemplate(datasource);
    }

    @Override
    public TimeEntry create(TimeEntry timeEntryToCreate) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        String insertQuery = "insert into time_entries (project_id, user_id, date, hours) values (?, ?, ?, ?)";
        jdbctemplate.update(con -> {
                    PreparedStatement ps = con.prepareStatement(insertQuery, new String[]{"ID"});
                    ps.setLong(1, timeEntryToCreate.getProjectId());
                    ps.setLong(2, timeEntryToCreate.getUserId());
                    ps.setDate(3, java.sql.Date.valueOf(timeEntryToCreate.getDate()));
                    ps.setInt(4, timeEntryToCreate.getHours());
                    return ps;
                }, keyHolder);

        Number key = keyHolder.getKey();

        return find(key.longValue());
    }

    @Override
    public TimeEntry find(long timeEntryId) {
        List<TimeEntry> timeEntries = jdbctemplate.query("select id, project_id, user_id, date, hours from time_entries where id =?", new Object[]{timeEntryId},
                (resultSet, i) -> {
                return new TimeEntry(resultSet.getLong("id"),
                        resultSet.getLong("project_id"),
                        resultSet.getLong("user_id"),
                        new java.sql.Date(resultSet.getDate("date").getTime()).toLocalDate(),
                        resultSet.getInt("hours"));
                });
        if (timeEntries.size() == 1) {
            return timeEntries.get(0);
        }
        return null;
    }

    @Override
    public List<TimeEntry> list() {
        String query = "select id, project_id, user_id, date, hours from time_entries order by id";
        List<TimeEntry> timeEntries = jdbctemplate.query(query,
                (resultSet, i) -> {
                    return new TimeEntry(resultSet.getLong("id"),
                            resultSet.getLong("project_id"),
                            resultSet.getLong("user_id"),
                            new java.sql.Date(resultSet.getDate("date").getTime()).toLocalDate(),
                            resultSet.getInt("hours"));
                });
        return timeEntries;
    }

    @Override
    public TimeEntry update(long timeEntryId, TimeEntry newTimeEntry) {
        String query = "update time_entries set project_id = ?, user_id = ?, date = ?, hours = ? where id = ?";
        int updatedRows = jdbctemplate.update(query, newTimeEntry.getProjectId(), newTimeEntry.getUserId(), java.sql.Date.valueOf(newTimeEntry.getDate()), newTimeEntry.getHours(), timeEntryId);
        if (updatedRows == 0) {
            return null;
        }
        return find(timeEntryId);
    }

    @Override
    public void delete(long timeEntryId) {
        String query = "delete from time_entries where id = " + timeEntryId;
        jdbctemplate.execute(query);
    }
}
