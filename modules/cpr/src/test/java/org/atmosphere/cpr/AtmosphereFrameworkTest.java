/*
 * Copyright 2014 Jason Burgess
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package org.atmosphere.cpr;

import org.atmosphere.util.ServletContextFactory;
import org.testng.annotations.Test;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import java.util.Collection;
import java.util.Enumeration;
import java.util.concurrent.atomic.AtomicInteger;

import static org.mockito.Mockito.mock;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;

public class AtmosphereFrameworkTest {

    @Test
    public void testBroadcasterFactory() throws Exception {
        AtmosphereFramework f = new AtmosphereFramework();
        f.setBroadcasterFactory(new DefaultBroadcasterFactory(DefaultBroadcaster.class, "NEVER", f.getAtmosphereConfig()));
        assertNotNull(f.getBroadcasterFactory());
    }

    @Test
    public void testServletContextFactory() throws ServletException {
        AtmosphereFramework f = new AtmosphereFramework();
        f.init();
        assertNotNull(ServletContextFactory.getDefault().getServletContext());

    }

    @Test
    public void testReload() throws ServletException {
        AtmosphereFramework f = new AtmosphereFramework();
        f.init();
        f.destroy();
        f.init();
        assertNotNull(f.getBroadcasterFactory());
    }

    @Test
    public void testAtmosphereServlet() throws ServletException {
        AtmosphereServlet s = new MyAtmosphereServlet();
        s.init(new ServletConfig() {
            @Override
            public String getServletName() {
                return "void";
            }

            @Override
            public ServletContext getServletContext() {
                return mock(ServletContext.class);
            }

            @Override
            public String getInitParameter(String name) {
                return null;
            }

            @Override
            public Enumeration<String> getInitParameterNames() {
                return null;
            }
        });
        assertNotNull(s);
    }

    @Test
    public void testAtmosphereFrameworkListener() throws ServletException {
        AtmosphereServlet s = new MyAtmosphereServlet();
        final AtomicInteger count = new AtomicInteger();
        s.framework().frameworkListener(new AtmosphereFrameworkListener() {
            @Override
            public void onPreInit(AtmosphereFramework f) {
                count.incrementAndGet();
            }

            @Override
            public void onPostInit(AtmosphereFramework f) {
                count.incrementAndGet();
            }

            @Override
            public void onPreDestroy(AtmosphereFramework f) {
                count.incrementAndGet();
            }

            @Override
            public void onPostDestroy(AtmosphereFramework f) {
                count.incrementAndGet();
            }
        });

        s.init(new ServletConfig() {
            @Override
            public String getServletName() {
                return "void";
            }

            @Override
            public ServletContext getServletContext() {
                return mock(ServletContext.class);
            }

            @Override
            public String getInitParameter(String name) {
                return null;
            }

            @Override
            public Enumeration<String> getInitParameterNames() {
                return null;
            }
        });
        s.destroy();
        assertEquals(count.get(), 4);
    }

    public class MyAtmosphereServlet extends AtmosphereServlet {

        @Override
        public void init(ServletConfig config) throws ServletException {

            super.init(config);
            framework.setBroadcasterFactory(new MyBroadcasterFactory());
        }

    }

    public final static class MyBroadcasterFactory implements BroadcasterFactory {

        @Override
        public void configure(Class<? extends Broadcaster> clazz, String broadcasterLifeCyclePolicy, AtmosphereConfig c) {
        }

        @Override
        public Broadcaster get() {
            return null;
        }

        @Override
        public Broadcaster get(Object id) {
            return null;
        }

        @Override
        public <T extends Broadcaster> T get(Class<T> c, Object id) {
            return null;
        }

        @Override
        public void destroy() {

        }

        @Override
        public boolean add(Broadcaster b, Object id) {
            return false;
        }

        @Override
        public boolean remove(Broadcaster b, Object id) {
            return false;
        }

        @Override
        public <T extends Broadcaster> T lookup(Class<T> c, Object id) {
            return null;
        }

        @Override
        public <T extends Broadcaster> T lookup(Class<T> c, Object id, boolean createIfNull) {
            return null;
        }

        @Override
        public <T extends Broadcaster> T lookup(Object id) {
            return null;
        }

        @Override
        public <T extends Broadcaster> T lookup(Object id, boolean createIfNull) {
            return null;
        }

        @Override
        public void removeAllAtmosphereResource(AtmosphereResource r) {

        }

        @Override
        public boolean remove(Object id) {
            return false;
        }

        @Override
        public Collection<Broadcaster> lookupAll() {
            return null;
        }

        @Override
        public BroadcasterFactory addBroadcasterListener(BroadcasterListener b) {
            return this;
        }

        @Override
        public BroadcasterFactory removeBroadcasterListener(BroadcasterListener b) {
            return this;
        }
    }
}
