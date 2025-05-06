package elib.dto;

import java.time.LocalDate;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Schema(description = "DTO заказа книги с дополнительными полями")
@Data
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PRIVATE, force = true)
public class BookOrderDto {
	@Schema(description = "ID заказа", example = "1")
    private Integer id;
    
    @Schema(description = "ID читателя, на которого оформлен заказ", example = "1")
    private Integer readerId;
    
    @Schema(description = "ФИО читателя, на которого оформлен заках", example = "John Smith")
    private String readerFullName;
    
    @Schema(description = "ID книги из заказа", example = "1")
    private Integer bookId;
    
    @Schema(description = "Название книги из заказа", example = "Book Name")
    private String bookname;
    
    @Schema(description = "Дата - день оформления заказа", example = "2025-05-06")
    private LocalDate startDate;
    
    @Schema(description = "Дата - срок сдачи книги из заказа", example = "2025-05-06")
    private LocalDate endDate;
}
