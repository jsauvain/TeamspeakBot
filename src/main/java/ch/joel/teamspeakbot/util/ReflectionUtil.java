package ch.joel.teamspeakbot.util;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;

public class ReflectionUtil {

	public static List<Field> getFieldsFromClass(Class clazz) {
		return Arrays.asList(clazz.getDeclaredFields());
	}

	public static void setValue(Field field, Object object, String stringValue) {
		try {
			field.setAccessible(true);
			Object value;
			if (field.getType().getCanonicalName().equals("int")) {
				value = Integer.parseInt(stringValue);
			} else {
				value = stringValue;
			}
			field.set(object, value);
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
	}

	public static void setValue(String field, Object object, String value) {
		try {
			setValue(object.getClass().getField(field), object, value);
		} catch (NoSuchFieldException e) {
			e.printStackTrace();
		}
	}


}
