package cv.dge.dge_api_rvcc.aplication.notification.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class DefaultReponseDTO {
    private String msg;
    private String status;


}
