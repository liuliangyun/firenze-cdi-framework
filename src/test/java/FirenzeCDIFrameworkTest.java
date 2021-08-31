import com.thoughtworks.fusheng.integration.junit5.FuShengTest;
import exceptions.CircularDependencyException;
import exceptions.InterfaceInjectException;
import testCases.circularDependency.MovieListerAInjectB;
import testCases.injectClassDependency.MovieListerInjectClassDependency;
import testCases.injectInterfaceDependency.*;
import testCases.withoutDependency.MovieListerWithoutDependency;
import static java.util.Objects.isNull;

@FuShengTest
public class FirenzeCDIFrameworkTest {
    private Container container;

    public void newContainer () {
        container = new FirenzeContainer();
    }

    public void registerImplementation (String implementation) {
        if ("ColonDelimitedMovieFinder".equals(implementation)) {
            container.registerImplementation(MovieFinderInterface.class, ColonDelimitedMovieFinder.class);
        } else if ("DatabaseMovieFinder".equals(implementation)) {
            container.registerImplementation(MovieFinderInterface.class, DatabaseMovieFinder.class);
        }
    }

    public boolean isSuccessGetComponent (String dependency) {
        if ("没有依赖".equals(dependency)) {
            MovieListerWithoutDependency lister = null;
            try {
                lister = (MovieListerWithoutDependency)
                        container.getComponent(MovieListerWithoutDependency.class);
            } catch (InterfaceInjectException e) {
                e.printStackTrace();
            } catch (CircularDependencyException e) {
                e.printStackTrace();
            }
            return !isNull(lister);
        } else if ("class".equals(dependency)){
            MovieListerInjectClassDependency lister = null;
            try {
                lister = (MovieListerInjectClassDependency)
                        container.getComponent(MovieListerInjectClassDependency.class);
            } catch (InterfaceInjectException e) {
                e.printStackTrace();
            } catch (CircularDependencyException e) {
                e.printStackTrace();
            }
            return !isNull(lister) && !isNull(lister.finder);
        } else if ("interface".equals(dependency)) {
            MovieListerInjectInterfaceDependency lister = null;
            try {
                lister = (MovieListerInjectInterfaceDependency)
                        container.getComponent(MovieListerInjectInterfaceDependency.class);
            } catch (InterfaceInjectException e) {
                e.printStackTrace();
            } catch (CircularDependencyException e) {
                e.printStackTrace();
            }
            return !isNull(lister) && !isNull(lister.finder);
        } else if ("@Named(\'data\') interface".equals(dependency)) {
            MovieListerInjectNotExistedNamedInterfaceDependency lister = null;
            try {
                lister = (MovieListerInjectNotExistedNamedInterfaceDependency)
                        container.getComponent(MovieListerInjectNotExistedNamedInterfaceDependency.class);
            } catch (InterfaceInjectException e) {
                e.printStackTrace();
            } catch (CircularDependencyException e) {
                e.printStackTrace();
            }
            return !isNull(lister) && !isNull(lister.finder);
        } else if ("@Named(\'database\') interface".equals(dependency)) {
            MovieListerInjectNamedInterfaceDependency lister = null;
            try {
                lister = (MovieListerInjectNamedInterfaceDependency)
                        container.getComponent(MovieListerInjectNamedInterfaceDependency.class);
            } catch (InterfaceInjectException e) {
                e.printStackTrace();
            } catch (CircularDependencyException e) {
                e.printStackTrace();
            }
            return !isNull(lister) && !isNull(lister.finder);
        } else if ("相互依赖".equals(dependency)) {
            MovieListerAInjectB lister = null;
            try {
                lister = (MovieListerAInjectB)
                        container.getComponent(MovieListerAInjectB.class);
            } catch (InterfaceInjectException e) {
                e.printStackTrace();
            } catch (CircularDependencyException e) {
                e.printStackTrace();
            }
            return !isNull(lister);
        }
        return false;
    }

    public MovieListerWithoutDependency getComponentForClassWithoutDependency () {
        MovieListerWithoutDependency lister = null;
        try {
            lister = (MovieListerWithoutDependency) container.getComponent(MovieListerWithoutDependency.class);
        } catch (InterfaceInjectException e) {
            e.printStackTrace();
        } catch (CircularDependencyException e) {
            e.printStackTrace();
        }
        return lister;
    }

    public boolean isSameComponent (Object component1, Object component2) {
        return !isNull(component1) && !isNull(component2) && component1.hashCode() == component2.hashCode();
    }

}
