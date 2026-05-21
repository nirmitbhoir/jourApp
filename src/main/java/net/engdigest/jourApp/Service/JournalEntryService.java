package net.engdigest.jourApp.Service;

import net.engdigest.jourApp.Entity.JournalEntry;
import net.engdigest.jourApp.Entity.User;
import net.engdigest.jourApp.Repository.JournalEntryRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class JournalEntryService {

    @Autowired
    private JournalEntryRepository journalEntryRepository;

    @Autowired
    private UserService userService;

    // ✅ CREATE ENTRY
    @Transactional
    public void saveEntry(JournalEntry journalEntry, String userName) {
        try {
            User user = userService.findByUserName(userName);

            if (user == null) {
                throw new RuntimeException("User not found");
            }

            journalEntry.setDate(LocalDateTime.now());

            JournalEntry saved = journalEntryRepository.save(journalEntry);

            user.getJournal_entries().add(saved);

            userService.saveUser(user); // ✅ FIXED: was saveNewUser — no password touch needed here

        } catch (Exception e) {
            throw new RuntimeException("Error while saving journal entry", e);
        }
    }

    // ✅ UPDATE ENTRY (generic save)
    public void saveEntry(JournalEntry journalEntry) {
        journalEntryRepository.save(journalEntry);
    }

    // ✅ GET ALL (admin/debug use)
    public List<JournalEntry> getAll() {
        return journalEntryRepository.findAll();
    }

    // ✅ FIND BY ID
    public Optional<JournalEntry> findById(ObjectId id) {
        return journalEntryRepository.findById(id);
    }

    // ✅ DELETE ENTRY (with ownership check)
    @Transactional
    public boolean deleteById(ObjectId id, String userName) {
        try {
            User user = userService.findByUserName(userName);

            if (user == null)
                return false;

            boolean removed = user.getJournal_entries()
                    .removeIf(x -> x.getId().equals(id));

            if (removed) {
                userService.saveUser(user); // ✅ FIXED: was saveNewUser — no password touch needed here
                journalEntryRepository.deleteById(id);
                return true;
            }

            return false;

        } catch (Exception e) {
            throw new RuntimeException("Error while deleting journal entry", e);
        }
    }

    // ✅ GET USER ENTRIES
    public List<JournalEntry> getEntriesOfUser(String userName) {
        User user = userService.findByUserName(userName);

        if (user == null) {
            throw new RuntimeException("User not found");
        }

        return user.getJournal_entries();
    }
}