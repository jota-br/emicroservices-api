package io.github.jotabrc.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

@Accessors(chain = true)
@Setter
@Getter
@NoArgsConstructor
public class ResponsePayload<T> {

    private String message;
    private ResponseBody<T> body;
}
