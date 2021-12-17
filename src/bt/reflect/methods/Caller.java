package bt.reflect.methods;

import bt.log.Log;

import java.lang.StackWalker.Option;
import java.lang.invoke.MethodType;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Arrays;

/**
 * @author &#8904
 *
 */
public class Caller
{
    public static String formatParameterValues(Object... values)
    {
        Log.entry(values);

        String ret = formatParameterValuesAtIndex(2, values);

        Log.exit(ret);

        return ret;
    }

    public static String formatParameterValuesAtIndex(int stackIndex, Object[] values)
    {
        Log.entry(stackIndex, values);

        String ret = "";

        var stack = StackWalker.getInstance(Option.RETAIN_CLASS_REFERENCE)
                               .walk(s -> s.skip(stackIndex)
                                           .findFirst())
                               .get();

        Class cls = stack.getDeclaringClass();
        String methodName = stack.getMethodName();
        MethodType type = stack.getMethodType();

        try
        {
            Method m = cls.getDeclaredMethod(methodName, type.parameterArray());
            m.setAccessible(true);

            Parameter p;

            for (int i = 0; i < m.getParameters().length; i ++ )
            {
                p = m.getParameters()[i];
                ret += "[" + p.getName() + " = ";

                if (i < values.length)
                {
                    ret += valueToString(values[i]);
                }

                ret += "]";
            }
        }
        catch (NoSuchMethodException | SecurityException e)
        {
            Log.error("Failed to format parameter values", e);
        }

        Log.exit(ret);

        return ret;
    }

    public static String formatCallerString(Object... values)
    {
        Log.entry(values);

        String ret = formatCallerStringAtIndex(2, values);

        Log.exit(ret);

        return ret;
    }

    public static String formatCallerStringAtIndex(int stackIndex, Object[] values)
    {
        Log.entry(stackIndex, values);

        var stack = StackWalker.getInstance(Option.RETAIN_CLASS_REFERENCE)
                               .walk(s -> s.skip(stackIndex)
                                           .findFirst())
                               .get();

        String className = stack.getClassName();
        String methodName = stack.getMethodName();
        MethodType type = stack.getMethodType();

        var str = new StringBuilder();
        str.append(className);
        str.append(".");
        str.append(methodName);
        str.append("(");

        Class param;

        for (int i = 0; i < type.parameterArray().length; i ++ )
        {
            param = type.parameterArray()[i];
            str.append(param.getSimpleName());

            if (i < values.length)
            {
                str.append(" = " + valueToString(values[i]));
            }

            str.append(", ");
        }

        String ret = str.toString();

        if (type.parameterCount() > 0)
        {
            ret = ret.substring(0, ret.length() - 2);
        }
        str.setLength(0);
        str.append(ret);
        str.append(") : ");
        str.append(stack.getLineNumber());

        ret = str.toString();

        Log.exit(ret);

        return ret;
    }

    protected static String valueToString(Object value)
    {
        Log.entry(value);

        String ret = "";

        if (value != null)
        {
            if (value.getClass().isArray())
            {
                if (value.getClass() == boolean[].class)
                {
                    ret += Arrays.toString((boolean[])value);
                }
                else if (value.getClass() == byte[].class)
                {
                    ret += Arrays.toString((byte[])value);
                }
                else if (value.getClass() == short[].class)
                {
                    ret += Arrays.toString((short[])value);
                }
                else if (value.getClass() == int[].class)
                {
                    ret += Arrays.toString((int[])value);
                }
                else if (value.getClass() == long[].class)
                {
                    ret += Arrays.toString((long[])value);
                }
                else if (value.getClass() == float[].class)
                {
                    ret += Arrays.toString((float[])value);
                }
                else if (value.getClass() == double[].class)
                {
                    ret += Arrays.toString((double[])value);
                }
                else if (value.getClass() == char[].class)
                {
                    ret += Arrays.toString((char[])value);
                }
                else
                {
                    ret += Arrays.toString((Object[])value);
                }
            }
            else
            {
                ret += value;
            }
        }
        else
        {
            ret = "null";
        }

        Log.exit(ret);

        return ret;
    }
}