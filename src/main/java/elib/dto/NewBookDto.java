package elib.dto;

import elib.jdbc.entity.Book.Genre;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Schema(description = "DTO книги для создания новой записи")
@Data
@AllArgsConstructor
@NoArgsConstructor(access=AccessLevel.PRIVATE, force=true)
public class NewBookDto {
	@Schema(description = "ID писателя - автора книги", example = "1")
	private Long writerId;
    
	@Schema(description = "Название книги", example = "New Name")
    @Size(max = 100, message = "The length of the string must not exceed 100 characters")
    @NotBlank(message = "Cannot be empty")
	private String name;
    
	@Schema(description = "Описание книги", example = "New Description")
	private String description;
	
	@Schema(description = "Общее количество копий книги", example = "10")
	@PositiveOrZero(message = "Total number of copies must be non-negative")
	private int totalCopies;
	
	@Schema(description = "Жанр книги", example = "HISTORY")
	private Genre genre;
}

