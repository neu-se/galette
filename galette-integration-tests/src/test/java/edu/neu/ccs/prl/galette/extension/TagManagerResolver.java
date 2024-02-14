package edu.neu.ccs.prl.galette.extension;

import org.junit.jupiter.api.extension.*;

class TagManagerResolver implements ParameterResolver, AfterEachCallback, BeforeEachCallback {
    @Override
    public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext)
            throws ParameterResolutionException {
        return parameterContext.getParameter().getType().equals(TagManager.class);
    }

    @Override
    public Object resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext)
            throws ParameterResolutionException {
        return getTagManager(extensionContext);
    }

    @Override
    public void afterEach(ExtensionContext context) {
        getTagManager(context).tearDown();
    }

    @Override
    public void beforeEach(ExtensionContext context) {
        getTagManager(context).setUp();
    }

    private TagManager getTagManager(ExtensionContext context) {
        ExtensionContext.Namespace namespace =
                ExtensionContext.Namespace.create(getClass(), context.getRequiredTestMethod());
        ExtensionContext.Store store = context.getStore(namespace);
        return store.getOrComputeIfAbsent(TagManager.class, TagManagerResolver::createTagManager, TagManager.class);
    }

    private static TagManager createTagManager(Object key) {
        try {
            String className = System.getProperty("flow.manager", GaletteTagManager.class.getName());
            return (TagManager) Class.forName(className).getConstructor().newInstance();
        } catch (ReflectiveOperationException e) {
            throw new ParameterResolutionException(e.getMessage());
        }
    }
}
