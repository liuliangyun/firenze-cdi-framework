package cdi;

import cdi.exceptions.CircularDependencyException;
import cdi.exceptions.InterfaceInjectException;

public interface Container {
    public Object getComponent(Class clazz) throws InterfaceInjectException, CircularDependencyException;
}
