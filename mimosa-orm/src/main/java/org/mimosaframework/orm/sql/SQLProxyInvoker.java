package org.mimosaframework.orm.sql;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class SQLProxyInvoker implements InvocationHandler {
    private ClassLoader loader;
    private Class<?>[] interfaces;
    private Object invoker;
    private Method[] methods;

    private Object proxyObject;

    public SQLProxyInvoker(ClassLoader loader, Class<?>[] interfaces, Object invoker) {
        this.loader = loader;
        this.interfaces = interfaces;
        this.invoker = invoker;

        this.methods = invoker.getClass().getMethods();

        this.buildProxy();
    }

    public SQLProxyInvoker(Class<?>[] interfaces, Object invoker) {
        this.loader = this.getClass().getClassLoader();
        this.interfaces = interfaces;
        this.invoker = invoker;

        this.methods = invoker.getClass().getMethods();

        this.buildProxy();
    }

    private void buildProxy() {
        proxyObject = Proxy.newProxyInstance(
                this.loader,
                this.interfaces,
                this
        );
    }

    public <T> T getInterface(Class<T> cls) {
        return (T) this.proxyObject;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

        if (this.methods != null) {
            if (method.getDeclaringClass().equals(UnifyBuilder.class)) {
                Method invoker = this.invoker.getClass().getMethod(method.getName(), method.getParameterTypes());
                return invoker.invoke(this.invoker, args);
            }

            Class[] params = method.getParameterTypes();
            for (Method invoker : this.methods) {
                Class[] invokerParams = invoker.getParameterTypes();

                if (invoker.getName().equals(method.getName())
                        && params.length == invokerParams.length) {
                    boolean isEq = true;
                    int i = 0;
                    for (Class p : invokerParams) {
                        if (!p.equals(params[i])) {
                            isEq = false;
                            break;
                        }
                        i++;
                    }

                    if (isEq) {
                        invoker.invoke(this.invoker, args);
                        return this.proxyObject;
                    }
                }
            }
        }

        return null;
    }
}
