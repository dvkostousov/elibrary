package elib.jdbc.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor 
@NoArgsConstructor
public class Book {
	private Long id;
	private Long writerId;
	private String name;
	private String description;
	private int totalCopies;
	private int borrowCopies;
	private Genre genre;
	
	public enum Genre {
		FICTION, SCIENCE, FANTASY, HISTORY,
	}
}
