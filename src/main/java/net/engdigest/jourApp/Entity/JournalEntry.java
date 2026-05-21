package net.engdigest.jourApp.Entity;

import lombok.*;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.Date;

@Document(collection = "journal_entries")

/*@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode
@Builder
*/
@Data
@NoArgsConstructor
public class JournalEntry {

    @Id
    private ObjectId id;   // ✅ PRIMARY KEY
    @NonNull
    private String title;
    private String content;
    private LocalDateTime date;

    // getters and setters


}
