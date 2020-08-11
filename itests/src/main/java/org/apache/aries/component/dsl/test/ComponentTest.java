/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.aries.component.dsl.test;

import org.apache.aries.component.dsl.OSGi;
import org.apache.aries.component.dsl.OSGiResult;
import org.apache.aries.component.dsl.builder.ComponentBuilder;
import org.junit.*;
import org.osgi.framework.BundleContext;
import org.osgi.framework.FrameworkUtil;
import org.osgi.framework.ServiceReference;
import org.osgi.framework.ServiceRegistration;
import org.osgi.service.cm.Configuration;
import org.osgi.service.cm.ConfigurationAdmin;
import org.osgi.service.cm.ManagedService;
import org.osgi.util.tracker.ServiceTracker;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;

import static org.apache.aries.component.dsl.OSGi.*;
import static org.apache.aries.component.dsl.Utils.highest;
import static org.junit.Assert.*;

/**
 * @author Carlos Sierra Andrés
 */
public class ComponentTest {

    static BundleContext _bundleContext =
        FrameworkUtil.getBundle(DSLTest.class).getBundleContext();
    private static ConfigurationAdmin _configurationAdmin;
    private static ServiceReference<ConfigurationAdmin> _configAdminServiceReference;

    @BeforeClass
    public static void setupClass() {
        _configAdminServiceReference = _bundleContext.getServiceReference(
            ConfigurationAdmin.class);

        _configurationAdmin = _bundleContext.getService(
            _configAdminServiceReference);
    }

    @AfterClass
    public static void tearDownClass() {
        _bundleContext.ungetService(_configAdminServiceReference);
    }

    @Test
    public void testComponentBuilder() throws Exception {
        OSGi<ServiceRegistration<Component>> program = ComponentBuilder.constructor(
            Component::new,
            configurations("org.components.MyComponent"),
            services(Service.class)
        ).optionalDependency(
            highestService(ServiceOptional.class), Component::setOptional
        ).optionalDependency(
            services(ServiceForList.class), Component::addService, Component::removeService
        ).register(
            new HashMap<>(), Component.class
        );

        ServiceTracker<Component, Component> serviceTracker =
                new ServiceTracker<>(_bundleContext, Component.class, null);

        serviceTracker.open();

        CountDownLatch countDownLatch = new CountDownLatch(1);

        _bundleContext.registerService(
                ManagedService.class, dictionary -> countDownLatch.countDown(),
                new Hashtable<String, Object>() {{
                    put("service.pid", "org.components.MyComponent");
                }});

        Configuration factoryConfiguration = null;

        try (OSGiResult run = program.run(_bundleContext)) {
            factoryConfiguration = _configurationAdmin.createFactoryConfiguration(
                    "org.components.MyComponent");
            factoryConfiguration.update(new Hashtable<>());

            countDownLatch.await(20, TimeUnit.SECONDS);

            assertNull(serviceTracker.getService());

            ServiceRegistration<Service> serviceRegistration =
                    _bundleContext.registerService(
                            Service.class, new Service(), new Hashtable<>());

            Component component = serviceTracker.waitForService(10 * 1000);

            assertNotNull(component);

            assertNull(component.getOptional());

            ServiceRegistration<ServiceOptional> serviceRegistration2 =
                    _bundleContext.registerService(
                            ServiceOptional.class, new ServiceOptional(),
                            new Hashtable<>());

            Thread.sleep(1000L);

            assertNotNull(component.getOptional());

            ServiceOptional serviceOptional = new ServiceOptional();

            ServiceRegistration<ServiceOptional> serviceRegistration3 =
                    _bundleContext.registerService(
                            ServiceOptional.class, serviceOptional,
                            new Hashtable<String, Object>() {{
                                put("service.ranking", 1);
                            }});

            assertEquals(serviceOptional, component.getOptional());

            serviceRegistration3.unregister();

            assertNotNull(component.getOptional());

            serviceRegistration2.unregister();

            assertNull(component.getOptional());

            ServiceRegistration<ServiceForList> serviceRegistration4 =
                    _bundleContext.registerService(
                            ServiceForList.class, new ServiceForList(),
                            new Hashtable<>());

            ServiceRegistration<ServiceForList> serviceRegistration5 =
                    _bundleContext.registerService(
                            ServiceForList.class, new ServiceForList(),
                            new Hashtable<>());

            assertEquals(2, component.getServiceForLists().size());

            serviceRegistration4.unregister();

            assertEquals(1, component.getServiceForLists().size());

            serviceRegistration5.unregister();

            assertEquals(0, component.getServiceForLists().size());

            serviceRegistration.unregister();

            Thread.sleep(1000L);

            assertNull(serviceTracker.getService());
        }
        catch (IOException ioe) {

        }
        catch (InterruptedException e) {
            Assert.fail("Timeout waiting for configuration");
        }
        finally {
            serviceTracker.close();

            if (factoryConfiguration != null) {
                factoryConfiguration.delete();
            }
        }
    }

    @Test
    public void testDependentComponentBuilder() throws IOException, InterruptedException {
        ComponentBuilder<DependentComponent> componentBuilder =
            ComponentBuilder.constructor(
                DependentComponent::new,
                ComponentBuilder.constructor(
                    Component::new,
                    configurations("org.components.MyComponent"),
                    services(Service.class)
                ).optionalDependency(
                    highestService(ServiceOptional.class), Component::setOptional
                ).optionalDependency(
                    services(ServiceForList.class), Component::addService, Component::removeService
                ).asOSGi(
                ),
                highestService(AnotherService.class)
        );

        AtomicReference<DependentComponent> dependentComponent = new AtomicReference<>();

        CountDownLatch countDownLatch = new CountDownLatch(1);

        _bundleContext.registerService(
                ManagedService.class, dictionary -> countDownLatch.countDown(),
                new Hashtable<String, Object>() {{
                    put("service.pid", "org.components.MyComponent");
                }});

        Configuration factoryConfiguration = null;

        try (OSGiResult run = componentBuilder.asOSGi().run(
                _bundleContext,
                dc -> {
                    dependentComponent.set(dc);
                    return () -> dependentComponent.set(null);
                }
            )
        ) {
            factoryConfiguration = _configurationAdmin.createFactoryConfiguration(
                    "org.components.MyComponent");
            factoryConfiguration.update(new Hashtable<>());

            countDownLatch.await(10, TimeUnit.SECONDS);

            ServiceRegistration<AnotherService> anotherServiceRegistration =
                _bundleContext.registerService(
                    AnotherService.class, new AnotherService(), new Hashtable<>());

            assertNull(dependentComponent.get());

            Service service = new Service();

            ServiceRegistration<Service> serviceRegistration =
                _bundleContext.registerService(
                    Service.class, service, new Hashtable<>());

            assertNotNull(dependentComponent.get());

            assertEquals(
                service,
                dependentComponent.get().getComponent().getMandatory());

            anotherServiceRegistration.unregister();

            assertNull(dependentComponent.get());

            serviceRegistration.unregister();

            assertNull(dependentComponent.get());
        }

    }

    @Test
    public void testComponentBuilderMandatoryDependenciesAndPostConstructAndPredestroy() {
        ComponentBuilder<BeanComponent> componentBuilder =
            ComponentBuilder.constructor(
                BeanComponent::new
            ).mandatoryDependency(
                highestService(Service.class), BeanComponent::setService
            ).postConstruct(
                BeanComponent::init
            ).predestroy(
                BeanComponent::destroy
            );

        AtomicReference<BeanComponent> beanComponentAtomicReference =
            new AtomicReference<>();

        try (OSGiResult run = componentBuilder.asOSGi(
             ).effects(
                beanComponentAtomicReference::set,
                __ -> beanComponentAtomicReference.set(null)
             ).run(
                 _bundleContext
             )
        ) {

            assertNull(beanComponentAtomicReference.get());

            ServiceRegistration<Service> serviceRegistration =
                _bundleContext.registerService(
                    Service.class, new Service(), new Hashtable<>());

            assertNotNull(beanComponentAtomicReference.get());

            BeanComponent beanComponent = beanComponentAtomicReference.get();

            assertTrue(beanComponent.isInitialized());
            assertFalse(beanComponent.isDestroyed());

            serviceRegistration.unregister();

            assertTrue(beanComponent.isInitialized());
            assertTrue(beanComponent.isDestroyed());

            assertNull(beanComponentAtomicReference.get());
        }
    }

    @Test
    public void testComponent() throws IOException {
        OSGi<?> program =
            combine(
                Component::new,
                configurations("org.components.MyComponent"),
                services(Service.class))
            .flatMap(component ->
                register(Component.class, component, new HashMap<>()).then(
                all(
                    dynamic(
                        highestService(ServiceOptional.class),
                        component::setOptional, c -> component.setOptional(null)),
                    dynamic(
                        services(ServiceForList.class),
                        component::addService, component::removeService)
            )));

        ServiceTracker<Component, Component> serviceTracker =
            new ServiceTracker<>(_bundleContext, Component.class, null);

        serviceTracker.open();

        CountDownLatch countDownLatch = new CountDownLatch(1);

        _bundleContext.registerService(
            ManagedService.class, dictionary -> countDownLatch.countDown(),
            new Hashtable<String, Object>() {{
                put("service.pid", "org.components.MyComponent");
            }});

        Configuration factoryConfiguration = null;

        try (OSGiResult run = program.run(_bundleContext)) {
            factoryConfiguration = _configurationAdmin.createFactoryConfiguration(
                "org.components.MyComponent");
            factoryConfiguration.update(new Hashtable<>());

            countDownLatch.await(10, TimeUnit.SECONDS);

            assertNull(serviceTracker.getService());

            ServiceRegistration<Service> serviceRegistration =
                _bundleContext.registerService(
                    Service.class, new Service(), new Hashtable<>());

            Component component = serviceTracker.waitForService(10 * 1000);

            assertNotNull(component);

            assertNull(component.getOptional());

            ServiceRegistration<ServiceOptional> serviceRegistration2 =
                _bundleContext.registerService(
                    ServiceOptional.class, new ServiceOptional(),
                    new Hashtable<>());

            Thread.sleep(1000L);

            assertNotNull(component.getOptional());

            ServiceOptional serviceOptional = new ServiceOptional();

            ServiceRegistration<ServiceOptional> serviceRegistration3 =
                _bundleContext.registerService(
                    ServiceOptional.class, serviceOptional,
                    new Hashtable<String, Object>() {{
                        put("service.ranking", 1);
                    }});

            assertEquals(serviceOptional, component.getOptional());

            serviceRegistration3.unregister();

            assertNotNull(component.getOptional());

            serviceRegistration2.unregister();

            assertNull(component.getOptional());

            ServiceRegistration<ServiceForList> serviceRegistration4 =
                _bundleContext.registerService(
                    ServiceForList.class, new ServiceForList(),
                    new Hashtable<>());

            ServiceRegistration<ServiceForList> serviceRegistration5 =
                _bundleContext.registerService(
                    ServiceForList.class, new ServiceForList(),
                    new Hashtable<>());

            assertEquals(2, component.getServiceForLists().size());

            serviceRegistration4.unregister();

            assertEquals(1, component.getServiceForLists().size());

            serviceRegistration5.unregister();

            assertEquals(0, component.getServiceForLists().size());

            serviceRegistration.unregister();

            assertNull(serviceTracker.getService());
        }
        catch (IOException ioe) {

        }
        catch (InterruptedException e) {
            Assert.fail("Timeout waiting for configuration");
        }
        finally {
            serviceTracker.close();

            if (factoryConfiguration != null) {
                factoryConfiguration.delete();
            }
        }
    }

    @Test
    public void testComponentApplicative() throws IOException, TimeoutException {
        OSGi<?> program =
            combine(
                Component::new,
                configurations("org.components.MyComponent"),
                services(Service.class)).
                flatMap(
                    comp ->
                register(Component.class, comp, new HashMap<>()).then(
                    all(
                        dynamic(
                            highestService(ServiceOptional.class),
                            comp::setOptional, c -> comp.setOptional(null)),
                        dynamic(
                            services(ServiceForList.class),
                            comp::addService, comp::removeService)
                )));

        ServiceTracker<Component, Component> serviceTracker =
            new ServiceTracker<>(_bundleContext, Component.class, null);

        serviceTracker.open();

        Configuration factoryConfiguration = null;

        try (OSGiResult run = program.run(_bundleContext)) {
            factoryConfiguration =
                _configurationAdmin.createFactoryConfiguration(
                    "org.components.MyComponent");

            factoryConfiguration.update(new Hashtable<>());

            Thread.sleep(1000);

            assertNull(serviceTracker.getService());

            ServiceRegistration<Service> serviceRegistration =
                _bundleContext.registerService(
                    Service.class, new Service(), new Hashtable<>());

            Component component = serviceTracker.waitForService(10 * 1000);

            assertNotNull(component);

            ServiceRegistration<Service> serviceRegistration10 =
                _bundleContext.registerService(
                    Service.class, new Service(), new Hashtable<>());

            assertEquals(2, serviceTracker.getServiceReferences().length);

            Configuration factoryConfiguration2 =
                _configurationAdmin.createFactoryConfiguration(
                    "org.components.MyComponent");

            factoryConfiguration2.update(new Hashtable<>());

            Thread.sleep(1000);

            assertEquals(4, serviceTracker.getServiceReferences().length);

            factoryConfiguration2.delete();

            Thread.sleep(1000);

            assertEquals(2, serviceTracker.getServiceReferences().length);

            serviceRegistration10.unregister();

            assertNull(component.getOptional());

            ServiceRegistration<ServiceOptional> serviceRegistration2 =
                _bundleContext.registerService(
                    ServiceOptional.class, new ServiceOptional(),
                    new Hashtable<>());

            Thread.sleep(1000L);

            assertNotNull(component.getOptional());

            ServiceOptional serviceOptional = new ServiceOptional();

            ServiceRegistration<ServiceOptional> serviceRegistration3 =
                _bundleContext.registerService(
                    ServiceOptional.class, serviceOptional,
                    new Hashtable<String, Object>() {{
                        put("service.ranking", 1);
                    }});

            assertEquals(serviceOptional, component.getOptional());

            serviceRegistration3.unregister();

            assertNotNull(component.getOptional());

            serviceRegistration2.unregister();

            assertNull(component.getOptional());

            ServiceRegistration<ServiceForList> serviceRegistration4 =
                _bundleContext.registerService(
                    ServiceForList.class, new ServiceForList(),
                    new Hashtable<>());

            ServiceRegistration<ServiceForList> serviceRegistration5 =
                _bundleContext.registerService(
                    ServiceForList.class, new ServiceForList(),
                    new Hashtable<>());

            assertEquals(2, component.getServiceForLists().size());

            serviceRegistration4.unregister();

            assertEquals(1, component.getServiceForLists().size());

            serviceRegistration5.unregister();

            assertEquals(0, component.getServiceForLists().size());

            serviceRegistration.unregister();

            assertNull(serviceTracker.getService());
        }
        catch (IOException ioe) {

        }
        catch (InterruptedException e) {
            Assert.fail("Timeout waiting for configuration");
        }
        finally {
            serviceTracker.close();

            if (factoryConfiguration != null) {
                factoryConfiguration.delete();
            }
        }
    }

    private static <T> OSGi<T> highestService(Class<T> clazz) {
        return service(highest(serviceReferences(clazz)));
    }

    public static <T> OSGi<Void> dynamic(
        OSGi<T> program, Consumer<T> bind, Consumer<T> unbind) {

        return program.foreach(bind, unbind);
    }

    private class Component {

        final Dictionary<String, ?> configuration;
        final Service mandatory;
        private final ArrayList<ServiceForList> _serviceForLists;
        ServiceOptional optional = null;

        public Component(Dictionary<String, ?> configuration, Service mandatory) {
            this.configuration = configuration;
            this.mandatory = mandatory;

            _serviceForLists = new ArrayList<>();
        }

        public Dictionary<String, ?> getConfiguration() {
            return configuration;
        }

        public Service getMandatory() {
            return mandatory;
        }

        public ServiceOptional getOptional() {
            return optional;
        }

        public void setOptional(ServiceOptional optional) {
            this.optional = optional;
        }

        public void addService(ServiceForList serviceForList) {
            _serviceForLists.add(serviceForList);
        }

        public void removeService(ServiceForList serviceForList) {
            _serviceForLists.remove(serviceForList);
        }

        public ArrayList<ServiceForList> getServiceForLists() {
            return _serviceForLists;
        }

    }

    private class DependentComponent {
        private Component _component;
        private AnotherService _anotherService;

        public DependentComponent(Component component, AnotherService anotherService) {
            _component = component;
            _anotherService = anotherService;
        }

        public Component getComponent() {
            return _component;
        }
    }

    private class BeanComponent {

        private boolean _init;
        private boolean _destroyed;

        public void setService(Service service) {
        }

        public void init() {
            _init = true;
        }

        public void destroy() {
            _destroyed = true;
        }

        public boolean isInitialized() {
            return _init;
        }

        public boolean isDestroyed() {
            return _destroyed;
        }

    }

    private class Service {}

    private class AnotherService {}

    private class ServiceOptional {}

    private class ServiceForList {}
}
