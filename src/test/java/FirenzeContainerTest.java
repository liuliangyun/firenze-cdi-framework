import cdi.Container;
import cdi.FirenzeContainer;
import cdi.exceptions.CircularDependencyException;
import cdi.exceptions.InterfaceInjectException;
import org.junit.jupiter.api.Test;
import testCases.Movie;
import testCases.circularDependency.MovieListerA;
import testCases.injectClass.MovieListerInjectClass;
import testCases.injectInterfaceWithMultiImpl.*;
import testCases.injectInterfaceWithOneImpl.MovieListerInjectInterfaceWithOneImpl;
import testCases.injectInterfaceWithoutImpl.MovieListerInjectInterfaceWithoutImpl;
import testCases.singleton.MovieListerWithSingleton;
import testCases.withoutDependency.MovieListerWithoutDependency;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class FirenzeContainerTest {
    @Test
    public void should_get_component_without_dependency_success() throws InterfaceInjectException, CircularDependencyException {
        // given configure container
        Container container = new FirenzeContainer();

        // when get component without inject dependency
        MovieListerWithoutDependency lister = (MovieListerWithoutDependency)
                container.getComponent(MovieListerWithoutDependency.class);

        // then call component method successfully
        List<Movie> movies = lister.moviesDirectedBy("zhang yi mou");
        assertEquals(movies, Arrays.asList(
                new Movie("zhang yi mou", "红高粱")
        ));
    }

    @Test
    public void should_get_component_which_inject_another_component_success() throws InterfaceInjectException, CircularDependencyException {
        // given configure container
        Container container = new FirenzeContainer();

        // when get component which inject another class
        MovieListerInjectClass lister = (MovieListerInjectClass)
                container.getComponent(MovieListerInjectClass.class);

        // then call inject class method successfully
        List<Movie> movies = lister.moviesDirectedBy("zhang yi mou");
        assertEquals(movies, Arrays.asList(
                new Movie("zhang yi mou", "红高粱"),
                new Movie("zhang yi mou", "秋菊打官司")
        ));
    }

    @Test
    public void should_throw_error_when_get_component_which_inject_interface_without_impl() {
        // given configure container
        Container container = new FirenzeContainer();

        // when get component which inject interface without impl
        try {
            container.getComponent(MovieListerInjectInterfaceWithoutImpl.class);
        } catch (Exception e) {
            assertEquals(InterfaceInjectException.class, e.getClass());
            assertEquals("there is no implementation for interface MovieFinderInterfaceWithoutImpl", e.getMessage());
        }
    }

    @Test
    public void should_get_component_which_inject_interface_with_only_one_impl_success() throws InterfaceInjectException, CircularDependencyException {
        // given configure container
        Container container = new FirenzeContainer();

        // when get component which inject interface with only one implementation class
        MovieListerInjectInterfaceWithOneImpl lister = (MovieListerInjectInterfaceWithOneImpl)
                container.getComponent(MovieListerInjectInterfaceWithOneImpl.class);

        // then call inject interface implementation class method successfully
        List<Movie> movies = lister.moviesDirectedBy("zhang yi mou");
        assertEquals(movies, Arrays.asList(
                new Movie("zhang yi mou", "红高粱"),
                new Movie("zhang yi mou", "秋菊打官司"),
                new Movie("zhang yi mou", "大红灯笼高高挂")
        ));
    }

    @Test
    public void should_throw_error_when_get_component_which_inject_interface_with_multi_impl() {
        // given configure container
        Container container = new FirenzeContainer();

        // when get component which inject interface with multi implementation class
        try {
            container.getComponent(MovieListerInjectInterfaceWithMultiImpl.class);
        } catch (Exception e) {
            assertEquals(InterfaceInjectException.class, e.getClass());
            assertEquals("there are multi implementation for interface MovieFinderInterfaceWithMultiImpl, must specify which implementation you want to inject", e.getMessage());
        }
    }

    @Test
    public void should_get_component_which_inject_interface_annotated_with_name_success() throws InterfaceInjectException, CircularDependencyException {
        // given configure container
        Container container = new FirenzeContainer();

        // when get component which inject inject interface with multi implementation class， but specified the implementation with @named
        MovieListerInjectNamedInterfaceWithMultiImpl lister = (MovieListerInjectNamedInterfaceWithMultiImpl)
                container.getComponent(MovieListerInjectNamedInterfaceWithMultiImpl.class);
        List<Movie> movies = lister.moviesDirectedBy("zhang yi mou");

        // then call inject class method successfully
        assertEquals(movies, Arrays.asList(
                new Movie("zhang yi mou", "红高粱"),
                new Movie("zhang yi mou", "秋菊打官司"),
                new Movie("zhang yi mou", "大红灯笼高高挂"),
                new Movie("zhang yi mou", "我的父亲母亲")
        ));
    }

    @Test
    public void should_throw_error_when_get_component_which_inject_interface_annotated_with_not_existed_name() {
        // given configure container
        Container container = new FirenzeContainer();

        // when get component which inject interface with implementation class， and the specified implementation with @named is not existed
        try {
            container.getComponent(MovieListerInjectNotExistedNamedInterfaceWithMultiImpl.class);
        } catch (Exception e) {
            assertEquals(InterfaceInjectException.class, e.getClass());
            assertEquals("data is not a implementation name of MovieFinderInterfaceWithMultiImpl", e.getMessage());
        }
    }

    @Test
    public void should_throw_error_when_there_are_circular_dependency() {
        // given configure container
        Container container = new FirenzeContainer();

        // when get component a which inject component b, but b is depend on a
        assertThrows(CircularDependencyException.class, () -> {
            container.getComponent(MovieListerA.class);
        });
    }

    @Test
    public void should_get_same_component_instance_which_annotated_with_singleton() throws CircularDependencyException, InterfaceInjectException {
        // given configure container
        Container container = new FirenzeContainer();

        // when get component annotated with singleton twice
        MovieListerWithSingleton lister1 = (MovieListerWithSingleton) container.getComponent(MovieListerWithSingleton.class);
        MovieListerWithSingleton lister2 = (MovieListerWithSingleton) container.getComponent(MovieListerWithSingleton.class);

        // lister1 and lister2 is the same instance
        assertEquals(lister1.hashCode(), lister2.hashCode());
    }
}
