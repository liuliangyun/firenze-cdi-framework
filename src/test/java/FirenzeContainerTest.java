import exceptions.CircularDependencyException;
import exceptions.InterfaceInjectException;
import org.junit.jupiter.api.Test;
import testCases.Movie;
import testCases.circularDependency.MovieListerAInjectB;
import testCases.injectClassDependency.MovieListerInjectClassDependency;
import testCases.injectInterfaceDependency.ColonDelimitedMovieFinder;
import testCases.injectInterfaceDependency.DatabaseMovieFinder;
import testCases.injectInterfaceDependency.MovieFinderInterface;
import testCases.injectInterfaceDependency.MovieListerInjectInterfaceDependency;
import testCases.injectInterfaceDependency.MovieListerInjectNamedInterfaceDependency;
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
        MovieListerInjectClassDependency lister = (MovieListerInjectClassDependency)
                container.getComponent(MovieListerInjectClassDependency.class);

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
        assertThrows(InterfaceInjectException.class, () -> {
            container.getComponent(MovieListerInjectInterfaceDependency.class);
        });
    }

    @Test
    public void should_get_component_which_inject_interface_with_only_one_impl_success() throws InterfaceInjectException, CircularDependencyException {
        // given configure container
        Container container = new FirenzeContainer();
        container.registerImplementation(MovieFinderInterface.class, ColonDelimitedMovieFinder.class);

        // when get component which inject interface with only one implementation class
        MovieListerInjectInterfaceDependency lister = (MovieListerInjectInterfaceDependency)
                container.getComponent(MovieListerInjectInterfaceDependency.class);

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
        container.registerImplementation(MovieFinderInterface.class, ColonDelimitedMovieFinder.class);
        container.registerImplementation(MovieFinderInterface.class, DatabaseMovieFinder.class);

        // when get component which inject interface with multi implementation class
        assertThrows(InterfaceInjectException.class, () -> {
            MovieListerInjectInterfaceDependency lister = (MovieListerInjectInterfaceDependency)
                    container.getComponent(MovieListerInjectInterfaceDependency.class);
        });
    }

    @Test
    public void should_get_component_which_inject_interface_annotated_with_name_success() throws InterfaceInjectException, CircularDependencyException {
        // given configure container
        Container container = new FirenzeContainer();
        container.registerImplementation(MovieFinderInterface.class, ColonDelimitedMovieFinder.class);
        container.registerImplementation(MovieFinderInterface.class, DatabaseMovieFinder.class);

        // when get component which inject inject interface with multi implementation class， but specified the implementation with @named
        MovieListerInjectNamedInterfaceDependency lister = (MovieListerInjectNamedInterfaceDependency)
                container.getComponent(MovieListerInjectNamedInterfaceDependency.class);
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
        container.registerImplementation(MovieFinderInterface.class, ColonDelimitedMovieFinder.class);

        // when get component which inject interface with implementation class， and the specified implementation with @named is not existed
        assertThrows(InterfaceInjectException.class, () -> {
            container.getComponent(MovieListerInjectNamedInterfaceDependency.class);
        });
    }

    @Test
    public void should_throw_error_when_there_are_circular_dependency() {
        // given configure container
        Container container = new FirenzeContainer();

        // when get component a which inject component b, but b is depend on a
        assertThrows(CircularDependencyException.class, () -> {
            container.getComponent(MovieListerAInjectB.class);
        });
    }

    @Test
    public void should_get_same_component_instance_which_annotated_with_singleton() throws CircularDependencyException, InterfaceInjectException {
        // given configure container
        Container container = new FirenzeContainer();

        // when get component annotated with singleton twice
        MovieListerWithoutDependency lister1 = (MovieListerWithoutDependency) container.getComponent(MovieListerWithoutDependency.class);
        MovieListerWithoutDependency lister2 = (MovieListerWithoutDependency) container.getComponent(MovieListerWithoutDependency.class);

        // lister1 and lister2 is the same instance
        assertEquals(lister1.hashCode(), lister2.hashCode());
    }
}
