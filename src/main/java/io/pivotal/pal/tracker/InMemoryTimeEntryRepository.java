package io.pivotal.pal.tracker;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

public class InMemoryTimeEntryRepository implements TimeEntryRepository {

    final Map<Long, TimeEntry> idToTimeEntry = new ConcurrentHashMap<Long, TimeEntry>();
    final private AtomicLong idCounter= new AtomicLong(1L);

    @Override
    public TimeEntry create(TimeEntry timeEntryToCreate) {
        long id = idCounter.getAndIncrement();
        timeEntryToCreate.setId(id);
        idToTimeEntry.put(id, timeEntryToCreate);
        return timeEntryToCreate;
    }

    @Override
    public TimeEntry find(long timeEntryId) {
        return idToTimeEntry.get(timeEntryId);
    }

    @Override
    public List<TimeEntry> list() {
        return this.idToTimeEntry.values().stream().collect(Collectors.toList());
    }

    @Override
    public TimeEntry update(long key, TimeEntry newEntry) {
        newEntry.setId(key);
        this.idToTimeEntry.replace(key, newEntry);
        return idToTimeEntry.get(key);
    }

    @Override
    public void delete(long timeEntryId) {
        idToTimeEntry.remove(timeEntryId);
    }
}
