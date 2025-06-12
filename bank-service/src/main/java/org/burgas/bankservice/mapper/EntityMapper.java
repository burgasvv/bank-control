package org.burgas.bankservice.mapper;

import org.burgas.bankservice.dto.Request;
import org.burgas.bankservice.dto.Response;
import org.burgas.bankservice.entity.AbstractEntity;
import org.burgas.bankservice.exception.EntityFieldsEmptyException;
import org.springframework.stereotype.Component;

@Component
public interface EntityMapper<T extends Request, S extends AbstractEntity, V extends Response> {

    default <D> D handleData(D requestData, D entityData) {
        return requestData == null || requestData == "" ? entityData : requestData;
    }

    default <D> D handleDataException(D requestData, String exceptionMessage) {
        if (requestData == null || requestData == "")
            throw new EntityFieldsEmptyException(exceptionMessage);
        return requestData;
    }

    S toEntity(T t);

    V toResponse(S s);
}
