package com.lotrel.ltserwer.lotrelCommons.common.protocol;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class BaseRequest {

    @NotBlank
    private String portalUserToken;
}
