package org.mimosaframework.rmi;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class RMIServer {
    public static void main(String[] args) throws RemoteException, MalformedURLException {
        CalcImpl calc = new CalcImpl();
        Registry registry = LocateRegistry.getRegistry(9898);
        System.out.println(registry);
        Naming.rebind("rmi://127.0.0.1:9898/calc_2", calc);
    }
}
