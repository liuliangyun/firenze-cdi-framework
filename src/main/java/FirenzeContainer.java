import annotations.Inject;
import annotations.Named;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

public class FirenzeContainer implements Container{
    private Map<String, Object> compMap = new HashMap<>();

    @Override
    public void registerComponent(Class clazz) {
        registerComponent(clazz, clazz, null);
    }

    @Override
    public void registerComponent(Class compKey, Class compImplementation, Object[] parameters) {
        String key = compImplementation.getSimpleName();

        //  处理类上加@Named的情况
        if (compImplementation.isAnnotationPresent(Named.class)) {
            Named namedAnnotation = (Named) compImplementation.getAnnotation(Named.class);
            key = namedAnnotation.value();
        }

        if (compMap.get(key) != null) {
            return;
        }

        Constructor[] constructors = compImplementation.getConstructors();
        try {
            // TODO: 只支持一个构造方法，后续支持多个构造方法，应该找到与parameters类型相同的构造方法
            Constructor constructor = constructors[0];
            Object comp = constructor.newInstance(null);

            // 处理属性上加注解@Inject的情况
            handleInjectField(compImplementation, comp);

            compMap.put(key, comp);
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    private void handleInjectField(Class compImplementation, Object comp) {
        Field[] fields = compImplementation.getDeclaredFields();
        for(Field field:fields){
            Inject inject = field.getAnnotation(Inject.class);
            if(inject != null) {
                // 处理属性上加注解@Named的情况
                Class clazz = field.getType();
                String key = clazz.getSimpleName();
                if (field.isAnnotationPresent(Named.class)) {
                    Named namedAnnotation = field.getAnnotation(Named.class);
                    key = namedAnnotation.value();
                }
                Object arg = getComponentForParam(key);
                try {
                    field.set(comp, arg);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private Object getComponentForParam(String key) {
        for (String mapKey: compMap.keySet()) {
            if (key.equals(mapKey)) {
                return compMap.get(key);
            }
        }
        return null;
    }

    @Override
    public Object getComponent(Class clazz) {
        //  处理类上加@Named的情况
        String key = clazz.getSimpleName();
        if (clazz.isAnnotationPresent(Named.class)) {
            Named namedAnnotation = (Named) clazz.getAnnotation(Named.class);
            key = namedAnnotation.value();
        }
        return compMap.get(key);
    }
}
