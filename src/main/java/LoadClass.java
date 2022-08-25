import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class LoadClass extends ClassLoader {

    String className = null;
    String methodName = null;
    byte[] data;


    public LoadClass(String className, String methodName, byte[] data) {
        this.className = className;
        this.methodName = methodName;
        this.data = data;
    }

    public String load() {

        String output = "";
        Class newClass = defineClass(className, data, 0, data.length);
        Method method = null;

        try {
            method = newClass.getMethod(methodName, null);
            Object o = method.invoke(null, null);
            output = (String) o;
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            System.out.println("Error invoking class method");
            throw new RuntimeException(e);
        }

        return output;
    }


}
