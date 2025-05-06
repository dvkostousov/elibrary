package elib.dto;

import java.util.List;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PRIVATE, force = true)
public class ReaderDto {
    private Integer id;
    private String fullName;
    private String address;
    private String contact;

    private List<ReadersBookOrderDto> bookOrders;
}

