package elib.dto;

import elib.jdbc.entity.Book.Genre;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor(access=AccessLevel.PRIVATE, force=true)
public class WritersBookDto {
	private Long id;
    private String name;
    private String description;
    private Genre genre;
}
