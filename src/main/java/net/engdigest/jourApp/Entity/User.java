package net.engdigest.jourApp.Entity;

import lombok.*;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import net.engdigest.jourApp.Entity.JournalEntry;  // ✅ ADD THIS IMPORT

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Document(collection = "User")
@Builder
@Data

@NoArgsConstructor
@AllArgsConstructor
public class User {
    @Id
    private ObjectId id;   // ✅ PRIMARY KEY
    @Indexed(unique = true)
    @NonNull
    private String userName;
    @NonNull
    private String password;
    @DBRef //Connect entity to each user
    private List<JournalEntry> journal_entries = new ArrayList<>();   // Lombok also generating get method with Journal_entrries()
    // getters and setters
    private List<String> roles;
}