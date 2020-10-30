package bt.reflect.classes;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;

public final class Classes
{
    public static <T> T newInstance(Class<T> cls)
    {
        return newInstance(cls, new Class[]{}, new Object[]{});
    }

    public static <T> T newInstance(Class<T> cls, Class<?>[] parameterTypes, Object[] parameterValues)
    {
        T obj = null;

        if (parameterTypes.length != parameterValues.length)
        {
            throw new IllegalArgumentException("Length of paramater type array and paramater value array must be the same.");
        }

        var construct = getConstructor(cls, parameterTypes);

        if (construct != null)
        {
            try
            {
                construct.setAccessible(true);
                obj = construct.newInstance(parameterValues);
            }
            catch (InstantiationException | IllegalAccessException | InvocationTargetException e)
            {
                e.printStackTrace();
            }
        }

        return obj;
    }

    public static <T> Constructor<T> getConstructor(Class<T> cls, Class<?>... parameters)
    {
        Class<?> currentClass = cls;

        while (currentClass != null)
        {
            try
            {
                return (Constructor<T>)currentClass.getDeclaredConstructor(parameters);
            }
            catch (NoSuchMethodException e)
            {
                currentClass = currentClass.getSuperclass();
            }
        }

        return null;
    }
}