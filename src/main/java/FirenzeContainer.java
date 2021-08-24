import annotations.Inject;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

public class FirenzeContainer implements Container{
    private Map<Class, Object> compMap = new HashMap<>();

    @Override
    public void registerComponent(Class clazz) {
        registerComponent(clazz, clazz, null);
    }

    @Override
    public void registerComponent(Class compKey, Class compImplementation, Object[] parameters) {
        if (compMap.get(compKey) != null) {
            return;
        }

        Constructor[] constructors = compImplementation.getConstructors();
        try {
            // TODO: 只支持一个构造方法，后续支持多个构造方法，应该找到与parameters类型相同的构造方法
            Constructor constructor = constructors[0];
            Object comp = constructor.newInstance(null);

            // 处理注解Inject
            handleInject(compImplementation, comp);

            compMap.put(compKey, comp);
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    private void handleInject(Class compImplementation, Object comp) {
        Field[] fields = compImplementation.getDeclaredFields();
        for(Field field:fields){
            Inject inject = field.getAnnotation(Inject.class);
            if(inject != null) {
                Class clazz = field.getType();
                Object arg = getComponentForParam(clazz);
                try {
                    field.set(comp, arg);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private Object getComponentForParam(Class param) {
        for (Class key: compMap.keySet()) {
            if (param.isAssignableFrom(key)) {
                return compMap.get(key);
            }
        }
        return null;
    }

    @Override
    public Object getComponent(Class clazz) {
        return compMap.get(clazz);
    }
}
