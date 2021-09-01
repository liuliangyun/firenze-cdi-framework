import com.thoughtworks.fusheng.integration.junit5.FuShengTest;
import testCases.circularDependency.MovieListerAInjectB;
import testCases.injectClassDependency.MovieListerInjectClassDependency;
import testCases.injectInterfaceDependency.*;
import testCases.withoutDependency.MovieListerWithoutDependency;

import java.util.HashMap;
import java.util.Map;

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

    public void registerImplementation (String implementation) {
        container.registerImplementation(MovieFinderInterface.class, implementationMap.get(implementation));
    }

    public boolean isSuccessGetComponent (String dependency) {
        if ("没有依赖".equals(dependency)) {
            MovieListerWithoutDependency lister = getComponentWithoutDependency();
            return !isNull(lister);
        } else if ("class".equals(dependency)){
            MovieListerInjectClassDependency lister = getComponentForInjectClass();
            return !isNull(lister) && !isNull(lister.finder);
        } else if ("interface".equals(dependency)) {
            MovieListerInjectInterfaceDependency lister = getComponentForInjectInterface();
            return !isNull(lister) && !isNull(lister.finder);
        } else if ("@Named(\'data\') interface".equals(dependency)) {
            MovieListerInjectNotExistedNamedInterfaceDependency lister = getComponentForInjectInterfaceWithNotExistedName();
            return !isNull(lister) && !isNull(lister.finder);
        } else if ("@Named(\'database\') interface".equals(dependency)) {
            MovieListerInjectNamedInterfaceDependency lister = getComponentForInjectInterfaceWithExistedName();
            return !isNull(lister) && !isNull(lister.finder);
        } else if ("相互依赖".equals(dependency)) {
            MovieListerAInjectB lister = getComponentForInjectCircular();
            return !isNull(lister);
        }
        return false;
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

    public MovieListerWithoutDependency getComponentWithoutDependency () {
        return (MovieListerWithoutDependency) getComponent(MovieListerWithoutDependency.class);
    }

    public MovieListerInjectClassDependency getComponentForInjectClass () {
        return (MovieListerInjectClassDependency) getComponent(MovieListerInjectClassDependency.class);
    }

    public MovieListerInjectInterfaceDependency getComponentForInjectInterface () {
        return (MovieListerInjectInterfaceDependency) getComponent(MovieListerInjectInterfaceDependency.class);
    }

    public MovieListerInjectNotExistedNamedInterfaceDependency getComponentForInjectInterfaceWithNotExistedName () {
        return (MovieListerInjectNotExistedNamedInterfaceDependency) getComponent(MovieListerInjectNotExistedNamedInterfaceDependency.class);
    }

    public MovieListerInjectNamedInterfaceDependency getComponentForInjectInterfaceWithExistedName () {
        return (MovieListerInjectNamedInterfaceDependency) getComponent(MovieListerInjectNamedInterfaceDependency.class);
    }

    public MovieListerAInjectB getComponentForInjectCircular () {
        return (MovieListerAInjectB) getComponent(MovieListerAInjectB.class);
    }

    public boolean isSameComponent (Object component1, Object component2) {
        return !isNull(component1) && !isNull(component2) && component1.hashCode() == component2.hashCode();
    }

}
