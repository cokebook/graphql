package org.cokebook.graphql.spring;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.SpringApplicationRunListener;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;

/**
 * @date 2019/12/10 16:25
 */
public class GraphQlRunListener implements SpringApplicationRunListener {

    private SpringApplication springApplication;
    private String[] args;

    public GraphQlRunListener(SpringApplication springApplication, String[] args) {
        this.springApplication = springApplication;
        this.args = args;
    }

    @Override
    public void starting() {

    }

    @Override
    public void environmentPrepared(ConfigurableEnvironment environment) {

    }

    @Override
    public void contextPrepared(ConfigurableApplicationContext context) {
        TypeWiringKeeperRegister beanFactoryPostProcessor = new TypeWiringKeeperRegister();
        beanFactoryPostProcessor.setApplicationContext(context);
        context.addBeanFactoryPostProcessor(beanFactoryPostProcessor);
    }

    @Override
    public void contextLoaded(ConfigurableApplicationContext context) {

    }

    @Override
    public void finished(ConfigurableApplicationContext context, Throwable exception) {

    }
}
