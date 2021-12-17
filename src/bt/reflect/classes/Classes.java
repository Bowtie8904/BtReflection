package bt.reflect.classes;

import bt.log.Log;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public final class Classes
{
    public static <T> T newInstance(Class<T> cls)
    {
        Log.entry(cls);

        T obj = newInstance(cls, new Class[]{}, new Object[]{});

        Log.exit(obj);

        return obj;
    }

    public static <T> T newInstance(Class<T> cls, Class<?>[] parameterTypes, Object[] parameterValues)
    {
        Log.entry(cls, parameterTypes, parameterValues);

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
                Log.error("Failed to create new instance for class {}", cls.getName());
            }
        }

        Log.exit(obj);

        return obj;
    }

    public static <T> Constructor<T> getConstructor(Class<T> cls, Class<?>... parameters)
    {
        Log.entry(cls, parameters);

        Class<?> currentClass = cls;
        Constructor<T> constructor = null;

        while (currentClass != null)
        {
            try
            {
                constructor =  (Constructor<T>)currentClass.getDeclaredConstructor(parameters);
                break;
            }
            catch (NoSuchMethodException e)
            {
                currentClass = currentClass.getSuperclass();
            }
        }

        Log.exit(constructor);

        return constructor;
    }
}