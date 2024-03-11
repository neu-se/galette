package edu.neu.ccs.prl.galette.bench.extension;

import java.lang.reflect.Field;
import java.util.List;
import java.util.function.Predicate;
import org.junit.jupiter.api.extension.*;
import org.junit.platform.commons.support.ModifierSupport;
import org.junit.platform.commons.util.ReflectionUtils;

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
        TagManager manager = getTagManager(context);
        manager.setUp();
        initializeInstanceFields(context, TagManager.class, manager);
    }

    private TagManager getTagManager(ExtensionContext context) {
        ExtensionContext.Namespace namespace =
                ExtensionContext.Namespace.create(getClass(), context.getRequiredTestMethod());
        ExtensionContext.Store store = context.getStore(namespace);
        return store.getOrComputeIfAbsent(TagManager.class, TagManagerResolver::createTagManager, TagManager.class);
    }

    private static TagManager createTagManager(Object key) {
        try {
            String name = System.getProperty("flow.manager");
            if (name == null) {
                throw new ParameterResolutionException("Missing value for system property: 'flow.manager'");
            }
            return (TagManager) Class.forName(name).getConstructor().newInstance();
        } catch (ReflectiveOperationException e) {
            throw new ParameterResolutionException(e.getMessage());
        }
    }

    static <T> void initializeInstanceFields(ExtensionContext context, Class<T> clazz, T value) {
        Predicate<Field> predicate = ModifierSupport::isNotStatic;
        predicate = predicate.and(field -> field.getType().equals(clazz));
        List<Field> fields = ReflectionUtils.findFields(
                context.getRequiredTestClass(), predicate, ReflectionUtils.HierarchyTraversalMode.TOP_DOWN);
        fields.forEach(field -> {
            try {
                field.setAccessible(true);
                field.set(context.getRequiredTestInstance(), value);
            } catch (ReflectiveOperationException e) {
                throw new ParameterResolutionException(e.getMessage());
            }
        });
    }
}
