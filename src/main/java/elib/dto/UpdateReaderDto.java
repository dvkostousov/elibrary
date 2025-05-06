package elib.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Schema(description = "DTO читателя для обновления существующей записи")
@Data
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PRIVATE, force = true)
public class UpdateReaderDto {
	@Schema(description = "ФИО читателя", example = "John Smit")
	@Size(max = 100, message = "The length of the string must not exceed 100 characters")
    private String fullName;
	
	@Schema(description = "Адрес читателя", example = "High Street London SW1A 1AA United Kingdom")
	@Size(max = 100, message = "The length of the string must not exceed 100 characters")
    private String address;
	
	@Schema(description = "Контакт читателя", example = "+7 900 000 00 00")
	@Size(max = 100, message = "The length of the string must not exceed 100 characters")
    private String contact;
}
