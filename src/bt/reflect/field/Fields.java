package bt.reflect.field;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

/**
 * Basic utilities involving fields.
 * 
 * @author &#8904
 */
public final class Fields
{
    /**
     * Gets all fields of the given class (and its super class/es).
     * 
     * @param cls
     *            The class to look in.
     * @return The list of fields that were found.
     */
    public static List<Field> getAllFields(Class<?> cls)
    {
        List<Field> fields = new ArrayList<>();
        Class<?> currentClass = cls;

        while (currentClass != null)
        {
            for (Field field : currentClass.getDeclaredFields())
            {
                fields.add(field);
            }

            currentClass = currentClass.getSuperclass();
        }

        return fields;
    }

    /**
     * Gets the first field of the given class (and all its super classes) that matched the given condition.
     *
     * @param cls
     * @param condition
     * @return The first found field or null.
     */
    public static Field getField(Class<?> cls, Predicate<Field> condition)
    {
        Field field = null;
        Class<?> currentClass = cls;

        while (currentClass != null)
        {
            for (Field f : currentClass.getDeclaredFields())
            {
                if (condition.test(f))
                {
                    field = f;
                    break;
                }
            }

            currentClass = currentClass.getSuperclass();
        }

        return field;
    }
}