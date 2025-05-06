package elib.dto;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonFormat;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Schema(description = "DTO заказа книги для обновления существующей записи")
@Data
@AllArgsConstructor
@NoArgsConstructor(access=AccessLevel.PRIVATE, force=true)
public class UpdateBookOrderDto {
	@Schema(description = "Дата - срок сдачи книги из заказа", example = "2025-05-06")
	@JsonFormat(pattern = "yyyy-MM-dd")
	private LocalDate endDate;
}
