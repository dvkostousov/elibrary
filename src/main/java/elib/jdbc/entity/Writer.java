package elib.jdbc.entity;

import io.swagger.v3.oas.annotations.media.Schema;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Schema(description = "Модель данных писателя")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Writer {
	@Schema(description = "ID писателя", example = "1")
	private Long id;
	
	@Schema(description = "ФИО писателя", example = "William Shakespearee")
	private String fullName;
	
	@Schema(description = "Биография писателя", example = "Writer's bio: ...")
	private String bio;
}
