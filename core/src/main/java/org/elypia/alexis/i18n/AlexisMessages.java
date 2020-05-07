package org.elypia.alexis.i18n;

import org.apache.deltaspike.core.api.message.*;

@MessageBundle
public interface AlexisMessages {

    @MessageTemplate("{accuracy}")
    String accuracy();
}
