package elib.dto;

import java.time.LocalDate;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Schema(description = "DTO заказа книги для создания новой записи")
@Data
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PRIVATE, force = true)
public class NewBookOrderDto {
	@Schema(description = "ID читателя, на которого оформлен заказ", example = "1")
    private Integer readerId;
	
	@Schema(description = "ID книги из заказа", example = "2")
    private Integer bookId;
	
	@Schema(description = "Дата - срок сдачи книги из заказа", example = "2025-05-06")
    private LocalDate endDate;
}
