package elib.dto;

import elib.jdbc.entity.Book.Genre;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor(access=AccessLevel.PRIVATE, force=true)
public class BookDto {
	private Long id;
	private Long writerId;
	private String writerFullName;
    private String name;
    private String description;
    private int totalCopies;
	private int borrowCopies;
	private Genre genre;
}
