package elib.dto;

import elib.jdbc.entity.Book.Genre;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Schema(description = "DTO книги для обновления существующей записи")
@Data
@AllArgsConstructor
@NoArgsConstructor(access=AccessLevel.PRIVATE, force=true)
public class UpdateBookDto {
	
	@Schema(description = "Название книги", example = "Updated Name")
	@Size(max = 100, message = "The length of the string must not exceed 100 characters")
    private String name;
	
	@Schema(description = "Описание книги", example = "Updated Description")
    private String description;
    
	@Schema(description = "Общее количество копий книги", example = "5")
    @PositiveOrZero(message = "Total number of copies must be non-negative")
    private Integer totalCopies;
    
	@Schema(description = "Жанр книги", example = "FICTION")
	private Genre genre;
}
