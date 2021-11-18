package cdi;

import cdi.annotations.Inject;
import cdi.annotations.Named;
import cdi.annotations.Singleton;
import cdi.exceptions.CircularDependencyException;
import cdi.exceptions.InterfaceInjectException;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.net.URL;
import java.util.*;

public class FirenzeContainer implements Container {
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
            comp = constructor.newInstance();
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
                    field.setAccessible(true);
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
            List<Class> allImplementations = getAllInterfaceAchieveClass(clazz);
            if (allImplementations.size() == 0) {
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

    private List<Class> getAllInterfaceAchieveClass(Class clazz){
        ArrayList<Class> list = new ArrayList<>();
        //判断是否是接口
        if (clazz.isInterface()) {
            try {
                ArrayList<Class> allClass = getAllClassByPath(clazz.getPackage().getName());
                /**
                 * 循环判断路径下的所有类是否实现了指定的接口
                 * 并且排除接口类自己
                 */
                for (int i = 0; i < allClass.size(); i++) {

                    //排除抽象类
                    if(Modifier.isAbstract(allClass.get(i).getModifiers())){
                        continue;
                    }
                    //判断是不是同一个接口
                    if (clazz.isAssignableFrom(allClass.get(i))) {
                        if (!clazz.equals(allClass.get(i))) {
                            list.add(allClass.get(i));
                        }
                    }
                }
            } catch (Exception e) {
                System.out.println("出现异常");
            }
        }
        return list;
    }

    private ArrayList<Class> getAllClassByPath(String packageName) {
        ArrayList<Class> list = new ArrayList<>();
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        String path = packageName.replace('.', '/');
        try {
            ArrayList<File> fileList = new ArrayList<>();
            Enumeration<URL> enumeration = classLoader.getResources(path);
            while (enumeration.hasMoreElements()) {
                URL url = enumeration.nextElement();
                fileList.add(new File(url.getFile()));
            }
            for (int i = 0; i < fileList.size(); i++) {
                list.addAll(findClass(fileList.get(i),packageName));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return list;
    }

    private ArrayList<Class> findClass(File file,String packageName) {
        ArrayList<Class> list = new ArrayList<>();
        if (!file.exists()) {
            return list;
        }
        File[] files = file.listFiles();
        for (File file2 : files) {
            if (file2.isDirectory()) {
                assert !file2.getName().contains(".");//添加断言用于判断
                ArrayList<Class> arrayList = findClass(file2, packageName + "." + file2.getName());
                list.addAll(arrayList);
            }else if(file2.getName().endsWith(".class")){
                try {
                    //保存的类文件不需要后缀.class
                    list.add(Class.forName(packageName + "." + file2.getName().substring(0, file2.getName().length()-6)));
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }
        return list;
    }
}
