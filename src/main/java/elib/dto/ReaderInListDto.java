package elib.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Schema(description = "DTO читателя для получения списка читателей")
@Data
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PRIVATE, force = true)
public class ReaderInListDto {
	@Schema(description = "ID читателя", example = "1")
    private Integer id;
    
    @Schema(description = "ФИО читателя", example = "John Smith")
    private String fullName;
    
    @Schema(description = "Адрес читателя", example = "High Street London SW1A 1AA United Kingdom")
    private String address;
    
    @Schema(description = "Контакт читателя", example = "+7 900 000 00 00")
    private String contact;
}
