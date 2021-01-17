package org.example.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ValueAspectCached {

    private String className;

    private String methodName;

    private String methodArgs;
}
