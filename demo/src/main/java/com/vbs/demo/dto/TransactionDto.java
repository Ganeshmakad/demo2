package com.vbs.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.bind.annotation.CrossOrigin;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TransactionDto {
    double amount;
    int id;
}
