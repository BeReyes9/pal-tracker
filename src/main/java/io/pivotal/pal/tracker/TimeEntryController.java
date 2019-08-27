package io.pivotal.pal.tracker;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.websocket.server.PathParam;
import java.util.List;

@RestController
public class TimeEntryController {
    TimeEntryRepository timeEntryRepository;
    public TimeEntryController(TimeEntryRepository timeEntryRepository) {
        this.timeEntryRepository = timeEntryRepository;
    }

    @PostMapping("/time-entries")
    public ResponseEntity create(@RequestBody TimeEntry timeEntryToCreate) {
        TimeEntry entry = timeEntryRepository.create(timeEntryToCreate);
        return new ResponseEntity(entry, HttpStatus.CREATED) ;
    }

    @GetMapping("/time-entries/{timeEntryId}")
    public ResponseEntity<TimeEntry> read(@PathVariable("timeEntryId") Long timeEntryId) {
        TimeEntry entry = timeEntryRepository.find(timeEntryId);
        if(entry != null) {
            return new ResponseEntity<>(entry, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/time-entries")
    public ResponseEntity<List<TimeEntry>> list() {
        final List<TimeEntry> entries = timeEntryRepository.list();
        return new ResponseEntity<>(entries, HttpStatus.OK);
    }

    @PutMapping("/time-entries/{timeEntryId}")
    public ResponseEntity update(@PathVariable("timeEntryId") Long timeEntryId,@RequestBody TimeEntry expected) {
        final TimeEntry entry = timeEntryRepository.update(timeEntryId, expected);
        if(entry != null) {
            return new ResponseEntity<>(entry, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/time-entries/{timeEntryId}")
    public ResponseEntity delete(@PathVariable("timeEntryId") Long timeEntryId) {
        timeEntryRepository.delete(timeEntryId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
