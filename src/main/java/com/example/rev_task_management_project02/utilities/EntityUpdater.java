package com.example.rev_task_management_project02.utilities;

import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

@Component
public class EntityUpdater {

    public <T> T updateFields(T existingEntity, T newDetails) {
        Field[] fields = newDetails.getClass().getDeclaredFields();

        for (Field field : fields) {
            field.setAccessible(true);
            try {
                if (Modifier.isFinal(field.getModifiers())) {
                    continue;
                }

                Object newValue = field.get(newDetails);

                if (newValue != null) {
                    if (isNumericField(field.getType())) {
                        if (!isZero(newValue)) {
                            field.set(existingEntity, newValue);
                        }
                    } else {
                        field.set(existingEntity, newValue);
                    }
                }
            } catch (IllegalAccessException e) {
                throw new RuntimeException("Failed to update field: " + field.getName(), e);
            }
        }

        return existingEntity;
    }

    private boolean isNumericField(Class<?> fieldType) {
        return fieldType == int.class || fieldType == long.class || fieldType == float.class || fieldType == double.class;
    }

    private boolean isZero(Object value) {
        if (value instanceof Number) {
            return ((Number) value).doubleValue() == 0.0;
        }
        return false;
    }
}
