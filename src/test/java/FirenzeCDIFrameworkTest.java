import cdi.Container;
import cdi.FirenzeContainer;
import com.thoughtworks.fusheng.integration.junit5.FuShengTest;
import testCases.circularDependency.MovieListerA;
import testCases.injectClass.MovieListerInjectClass;
import testCases.injectInterfaceWithMultiImpl.*;
import testCases.injectInterfaceWithOneImpl.MovieListerInjectInterfaceWithOneImpl;
import testCases.singleton.MovieListerWithoutSingleton;
import testCases.singleton.MovieListerWithSingleton;
import testCases.withoutDependency.MovieListerWithoutDependency;

import java.util.*;

import static java.util.Objects.isNull;

@FuShengTest
public class FirenzeCDIFrameworkTest {
    private Container container;

    public void newContainer () {
        container = new FirenzeContainer();
    }

    public Object getComponentForDependency (String dependency) {
        Object lister = null;
        if ("没有依赖".equals(dependency)) {
            lister = getComponent(MovieListerWithoutDependency.class);
        } else if ("依赖类".equals(dependency)){
            lister = getComponent(MovieListerInjectClass.class);
        } else if ("依赖接口（只有一个实现类）".equals(dependency)) {
            lister = getComponent(MovieListerInjectInterfaceWithOneImpl.class);
        } else if ("依赖@Named标识的接口（有多个实现类）".equals(dependency)) {
            lister = getComponent(MovieListerInjectNamedInterfaceWithMultiImpl.class);
        } else if ("相互依赖".equals(dependency)) {
            lister = getComponent(MovieListerA.class);
        }
        return lister;
    }

    public String isGetListerSuccess (Object lister) {
        return !isNull(lister) ? "成功" : "失败";
    }

    public String getInjectedFinderClassName (Object lister, String dependency) {
        Object finder = null;
        if ("依赖类".equals(dependency)) {
            finder = ((MovieListerInjectClass) lister).getFinder();
        } else if ("依赖接口（只有一个实现类）".equals(dependency)) {
            finder = ((MovieListerInjectInterfaceWithOneImpl) lister).getFinder();
        } else if ("依赖@Named标识的接口（有多个实现类）".equals(dependency)) {
            finder = ((MovieListerInjectNamedInterfaceWithMultiImpl) lister).getFinder();
        }
        return isNull(finder) ? "" : finder.getClass().getSimpleName();
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
