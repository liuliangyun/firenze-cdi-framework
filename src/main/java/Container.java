import exceptions.CircularDependencyException;
import exceptions.InterfaceInjectException;

public interface Container {
    public Object getComponent(Class clazz) throws InterfaceInjectException, CircularDependencyException;
}
