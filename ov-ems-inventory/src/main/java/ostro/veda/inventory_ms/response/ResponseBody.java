package ostro.veda.inventory_ms.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.ArrayList;
import java.util.List;

@Accessors(chain = true)
@Setter
@Getter
@NoArgsConstructor
public class ResponseBody<T> {

    private List<T> data = new ArrayList<>();
}
