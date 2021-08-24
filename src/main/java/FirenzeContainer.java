import annotations.Inject;
import annotations.Named;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.*;

public class FirenzeContainer implements Container{
    private Map<Class, List<Class>> interfaceMap = new HashMap<>();

    @Override
    public Object getComponent(Class clazz) {
        Constructor[] constructors = clazz.getConstructors();
        try {
            Constructor constructor = constructors[0];
            Object comp = constructor.newInstance(null);

            // 处理属性上加注解@Inject的情况
            handleInjectField(clazz, comp);

            return comp;
        } catch (Exception e) {
            System.out.println(e);
        }
        return null;
    }

    private void handleInjectField(Class compImplementation, Object comp) {
        Field[] fields = compImplementation.getDeclaredFields();
        for(Field field:fields){
            Inject inject = field.getAnnotation(Inject.class);
            if(inject != null) {
                Class clazz = field.getType();
                String namedValue = getFieldNamedValue(field);
                Object arg = getComponentForField(clazz, namedValue);
                try {
                    field.set(comp, arg);
                } catch (IllegalAccessException e) {
                    System.out.println(e);
                }
            }
        }
    }

    private String getFieldNamedValue(Field field) {
        String value = null;
        if (field.isAnnotationPresent(Named.class)) {
            Named namedAnnotation = (Named) field.getAnnotation(Named.class);
            value = namedAnnotation.value();
        }
        return value;
    }

    private Object getComponentForField(Class clazz, String key) {
        Class fieldClazz = clazz;
        if (clazz.isInterface()) {
            List<Class> allImplementations = getAllImplementations(clazz);
            if (key == null) {
                fieldClazz = allImplementations.get(0);
            } else {
                fieldClazz = allImplementations.stream()
                        .filter(impl -> isSpecificImplementation(impl, key))
                        .findFirst()
                        .orElse(null);
            }
        }
        return getComponent(fieldClazz);
    }

    private boolean isSpecificImplementation(Class implementation, String key) {
        String name = getImplementationClassNamedValue(implementation);
        return Objects.equals(key, name);
    }

    private String getImplementationClassNamedValue(Class clazz) {
        String value = clazz.getSimpleName();
        if (clazz.isAnnotationPresent(Named.class)) {
            Named namedAnnotation = (Named) clazz.getAnnotation(Named.class);
            value = namedAnnotation.value();
        }
        return value;
    }

    private List<Class> getAllImplementations(Class clazz) {
        return interfaceMap.get(clazz);
    }

    @Override
    public void registerImplementation(Class clazzInterface, Class clazzImplementation) {
        if (interfaceMap.containsKey(clazzInterface)) {
            List<Class> implementations = interfaceMap.get(clazzInterface);
            implementations.add(clazzImplementation);
        } else {
            List<Class> implementations = new ArrayList<>();
            implementations.add(clazzImplementation);
            interfaceMap.put(clazzInterface, implementations);
        }
    }
}
