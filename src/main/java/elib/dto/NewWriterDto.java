package elib.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Schema(description = "DTO писателя для создания новой записи")
@Data
@AllArgsConstructor
@NoArgsConstructor(access=AccessLevel.PRIVATE, force=true)
public class NewWriterDto {
	@Schema(description = "ФИО писателя", example = "William Shakespeare")
	@NotBlank(message = "Cannot be empty")
	@Size(max = 100, message = "The length of the string must not exceed 100 characters")
    private String fullName;
	
	@Schema(description = "Биография писателя", example = "New bio")
    private String bio;
}
