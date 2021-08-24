public interface Container {
    public void registerComponent(Class clazz);

    public void registerComponent(Class compKey, Class compImplementation, Object[] params);

    public Object getComponent(Class clazz);
}
