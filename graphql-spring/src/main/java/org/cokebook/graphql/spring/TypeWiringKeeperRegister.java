package org.cokebook.graphql.spring;

import org.cokebook.graphql.TypeWiringKeeper;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * GraphQL BeanFactory PostProcessor
 * <p>
 * 该操作的目的在于提前将特定 BeanPostProcessor 注入到 BeanFactory 中, 避免采用依赖常规 BeanDefinition 形式
 * 注入导致创建顺序难于控制问题.
 * Note: 为什么实现 {@link BeanDefinitionRegistryPostProcessor} 接口而不是更基础的 {@link org.springframework.beans.factory.config.BeanFactoryPostProcessor} 接口
 * 目的在于尽量早的是该类逻辑靠前执行 {@linkplain PostProcessorRegistrationDelegate#invokeBeanFactoryPostProcessors }
 *
 * @date 2019/12/10 16:27
 */
public class TypeWiringKeeperRegister implements BeanDefinitionRegistryPostProcessor, ApplicationContextAware {

    private static final String TYPE_WIRING_KEEPER_BEAN_NAME = TypeWiringKeeper.class.getName() + ".bean";


    private ApplicationContext context;

    private final AtomicBoolean processed = new AtomicBoolean(false);

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
        registerTypeWiringKeeper(beanFactory);
    }

    private void registerTypeWiringKeeper(ConfigurableListableBeanFactory beanFactory) {
        if (!processed.getAndSet(true)) {
            SimpleTypeWiringKeeper adapter = new SimpleTypeWiringKeeper();
            beanFactory.addBeanPostProcessor(adapter);
            beanFactory.registerResolvableDependency(TypeWiringKeeper.class, new TypeWiringKeeperAdapter(adapter));
        }
    }

    @Override
    public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry) throws BeansException {
        // 确保尽可能早的将 SimpleTypeWiringKeeper 注入到 BeanFactory 中
        if (registry instanceof ConfigurableListableBeanFactory) {
            registerTypeWiringKeeper((ConfigurableListableBeanFactory) registry);
        }
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.context = applicationContext;
    }


    public static class TypeWiringKeeperAdapter implements TypeWiringKeeper {

        private TypeWiringKeeper keeper;

        public TypeWiringKeeperAdapter(TypeWiringKeeper keeper) {
            this.keeper = keeper;
        }

        @Override
        public Map<Method, String> typeWiringMethods() {
            return keeper.typeWiringMethods();
        }
    }

}
