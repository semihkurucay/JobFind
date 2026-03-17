package com.semihkurucay.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DtoCompanyCommentIU {

    private Integer point;

    @NotEmpty(message = "Lütfen yorum alanını boş geçmeyin.")
    @Size(min = 3, message = "En az 3 karakter girin yorum için.")
    private String comment;
}
