package io.pivotal.pal.tracker;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;

@SpringBootApplication
public class PalTrackerApplication {

    //This is for illustration purpose only.
//    @Bean
//    JdbcTemplate jdbcTemplate(DataSource datasource) {
//        return new JdbcTemplate(datasource);
//    }
//
//
//
//    @Bean
//    TimeEntryRepository timeEntryRepository(JdbcTemplate jdbcTemplate) {
//        return new JdbcTimeEntryRepository(jdbcTemplate);
//    }
//
   @Bean
    TimeEntryRepository timeEntryRepository(DataSource datasource) {
        return new JdbcTimeEntryRepository(datasource);
    }

    public static void main(String[] args) {
        SpringApplication.run(PalTrackerApplication.class, args);
    }
}
