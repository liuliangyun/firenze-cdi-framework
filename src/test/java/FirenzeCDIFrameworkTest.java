import com.thoughtworks.fusheng.integration.junit5.FuShengTest;
import testCases.circularDependency.MovieListerA;
import testCases.injectClassDependency.MovieListerInjectClassDependency;
import testCases.injectInterfaceDependency.*;
import testCases.singleton.MovieListerWithoutSingleton;
import testCases.singleton.MovieListerWithSingleton;
import testCases.withoutDependency.MovieListerWithoutDependency;

import java.util.*;

import static java.util.Objects.isNull;

@FuShengTest
public class FirenzeCDIFrameworkTest {
    private Container container;
    private Map<String, Class> implementationMap = new HashMap<>(){{
        put("ColonDelimitedMovieFinder", ColonDelimitedMovieFinder.class);
        put("DatabaseMovieFinder", DatabaseMovieFinder.class);
    }};

    public void newContainer () {
        container = new FirenzeContainer();
    }

    public void registerImplementation (String implementations) {
        String[] array = implementations.split("和");
        Arrays.stream(array).forEach(impl -> container.registerImplementation(MovieFinderInterface.class, implementationMap.get(impl)));
    }

    public Object getComponentForDependency (String dependency) {
        Object lister = null;
        if ("没有依赖".equals(dependency)) {
            lister = getComponent(MovieListerWithoutDependency.class);
        } else if ("依赖类".equals(dependency)){
            lister = getComponent(MovieListerInjectClassDependency.class);
        } else if ("依赖接口".equals(dependency)) {
            lister = getComponent(MovieListerInjectInterfaceDependency.class);
        } else if ("依赖@Named标识的接口".equals(dependency)) {
            lister = getComponent(MovieListerInjectNamedInterfaceDependency.class);
        } else if ("相互依赖".equals(dependency)) {
            lister = getComponent(MovieListerA.class);
        }
        return lister;
    }

    public String isGetListerSuccess (Object lister) {
        return !isNull(lister) ? "成功" : "失败";
    }

    public String getInjectedFinderClassName (Object lister, String dependency) {
        if ("依赖类".equals(dependency)) {
            Object finder = ((MovieListerInjectClassDependency) lister).getFinder();
            return isNull(finder) ? "" : finder.getClass().getSimpleName();
        } else if ("依赖接口".equals(dependency)) {
            Object finder = ((MovieListerInjectInterfaceDependency) lister).getFinder();
            return isNull(finder) ? "" : finder.getClass().getSimpleName();
        } else if ("依赖@Named标识的接口".equals(dependency)) {
            Object finder = ((MovieListerInjectNamedInterfaceDependency) lister).getFinder();
            return isNull(finder) ? "" : finder.getClass().getSimpleName();
        }
        return "";
    }

    public Object getComponent (Class clazz) {
        Object lister = null;
        try {
            lister = container.getComponent(clazz);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return lister;
    }

    public List<Object> getComponentSeveralTimes (String isSingleton, String times) {
        int count = Integer.parseInt(times);
        List<Object> objects = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            if (isSingleton == "是") {
                objects.add(getComponent(MovieListerWithSingleton.class));
            } else {
                objects.add(getComponent(MovieListerWithoutSingleton.class));
            }
        }
        return objects;
    }

    public String isSameComponent (List<Object> components) {
        int code = components.get(0).hashCode();
        boolean result = components.stream().allMatch(component -> component.hashCode() == code);
        return result ? "相同" : "不同";
    }

}
