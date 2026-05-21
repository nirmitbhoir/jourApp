package net.engdigest.jourApp.Controller;

import net.engdigest.jourApp.Entity.JournalEntry;
import net.engdigest.jourApp.Entity.User;
import net.engdigest.jourApp.Service.JournalEntryService;
import net.engdigest.jourApp.Service.UserService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/journal")
public class JournalEntryControllerV2 {

    @Autowired
    private JournalEntryService journalEntryService;

    @Autowired
    private UserService userService;

    // GET ALL ENTRIES
    @GetMapping
    public ResponseEntity<?> getAllJournalEntriesOfUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String userName = auth.getName();

        User user = userService.findByUserName(userName);

        if (user == null) {
            return new ResponseEntity<>("User not found", HttpStatus.NOT_FOUND);
        }

        List<JournalEntry> entries = user.getJournal_entries();

        return new ResponseEntity<>(entries, HttpStatus.OK);
    }

    // ✅ CREATE ENTRY
    @PostMapping
    public ResponseEntity<?> createEntry(@RequestBody JournalEntry entry) {
        try {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            String userName = auth.getName();

            entry.setDate(LocalDateTime.now());

            journalEntryService.saveEntry(entry, userName);

            return new ResponseEntity<>(entry, HttpStatus.CREATED);

        } catch (Exception e) {
            return new ResponseEntity<>("Error creating entry", HttpStatus.BAD_REQUEST);
        }
    }

    // ✅ GET ENTRY BY ID
    @GetMapping("/{id}")
    public ResponseEntity<?> getEntryById(@PathVariable ObjectId id) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String userName = auth.getName();

        User user = userService.findByUserName(userName);

        boolean exists = user.getJournal_entries()
                .stream()
                .anyMatch(x -> x.getId().equals(id));

        if (!exists) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        Optional<JournalEntry> entry = journalEntryService.findById(id);

        return entry.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    // ✅ DELETE ENTRY
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteEntry(@PathVariable ObjectId id) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String userName = auth.getName();

        journalEntryService.deleteById(id, userName);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    // ✅ UPDATE ENTRY
    @PutMapping("/{id}")
    public ResponseEntity<?> updateEntry(@PathVariable ObjectId id,
                                         @RequestBody JournalEntry newEntry) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String userName = auth.getName();

        User user = userService.findByUserName(userName);

        boolean exists = user.getJournal_entries()
                .stream()
                .anyMatch(x -> x.getId().equals(id));

        if (!exists) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        Optional<JournalEntry> optional = journalEntryService.findById(id);

        if (optional.isPresent()) {
            JournalEntry old = optional.get();

            if (newEntry.getTitle() != null && !newEntry.getTitle().isEmpty()) {
                old.setTitle(newEntry.getTitle());
            }

            if (newEntry.getContent() != null && !newEntry.getContent().isEmpty()) {
                old.setContent(newEntry.getContent());
            }

            journalEntryService.saveEntry(old);

            return new ResponseEntity<>(old, HttpStatus.OK);
        }

        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}