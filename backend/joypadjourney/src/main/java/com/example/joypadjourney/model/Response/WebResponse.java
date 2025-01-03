package com.example.joypadjourney.model.Response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class WebResponse<T> {

    private T data;
    
    private String errors;
}
