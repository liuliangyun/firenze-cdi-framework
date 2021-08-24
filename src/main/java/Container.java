public interface Container {
    public Object getComponent(Class clazz);

    public void registerImplementation(Class clazzInterface, Class clazzImplementation);
}
