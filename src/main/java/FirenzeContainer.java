import annotations.Inject;
import annotations.Named;
import annotations.Singleton;
import exceptions.CircularDependencyException;
import exceptions.InterfaceInjectException;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.*;

public class FirenzeContainer implements Container{
    private Map<Class, List<Class>> interfaceMap = new HashMap<>();
    private Set<Class> generatingClass = new HashSet<>();
    private Map<String, Object> singletonComponentMap = new HashMap<>();

    @Override
    public Object getComponent(Class clazz)
            throws InterfaceInjectException, CircularDependencyException {
        if (generatingClass.contains(clazz)) {
            throw new CircularDependencyException("there are circular dependency on " + clazz.getSimpleName());
        }

        generatingClass.add(clazz);
        if (clazz.isAnnotationPresent(Singleton.class)) {
            String name = getClassNamedValue(clazz);
            if (singletonComponentMap.get(name) != null) {
                generatingClass.remove(clazz);
                return singletonComponentMap.get(name);
            }
            Object instance = newInstance(clazz);
            singletonComponentMap.put(name, instance);
            generatingClass.remove(clazz);
            return instance;
        }

        Object instance = newInstance(clazz);
        generatingClass.remove(clazz);
        return instance;
    }

    private Object newInstance(Class clazz)
            throws InterfaceInjectException, CircularDependencyException {
        Constructor[] constructors = clazz.getConstructors();
        Constructor constructor = constructors[0];
        Object comp = null;
        try {
            comp = constructor.newInstance(null);
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }

        // 处理属性上加注解@Inject的情况
        handleInjectField(clazz, comp);

        return comp;
    }

    private void handleInjectField(Class compImplementation, Object comp)
            throws InterfaceInjectException, CircularDependencyException {
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
                    e.printStackTrace();
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

    private Object getComponentForField(Class clazz, String key)
            throws InterfaceInjectException, CircularDependencyException {
        Class fieldClazz = clazz;
        if (clazz.isInterface()) {
            List<Class> allImplementations = getAllImplementations(clazz);
            if (allImplementations == null) {
                throw new InterfaceInjectException("there is no implementation for interface " + clazz.getSimpleName());
            }
            if (key == null) {
                if (allImplementations.size() > 1) {
                    throw new InterfaceInjectException("there are multi implementation for interface " + clazz.getSimpleName()
                            + ", must specify which implementation you want to inject");
                }
                fieldClazz = allImplementations.get(0);
            } else {
                fieldClazz = allImplementations.stream()
                        .filter(impl -> isSpecificImplementation(impl, key))
                        .findFirst()
                        .orElse(null);

                if (fieldClazz == null) {
                    throw new InterfaceInjectException(key + " is not a implementation name of " + clazz.getSimpleName());
                }
            }
        }
        return getComponent(fieldClazz);
    }

    private boolean isSpecificImplementation(Class implementation, String key) {
        String name = getClassNamedValue(implementation);
        return Objects.equals(key, name);
    }

    private String getClassNamedValue(Class clazz) {
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
