package org.mimosaframework.rmi;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class RMIRegisterServer {
    public static void main(String[] args) throws RemoteException, MalformedURLException, NotBoundException {
        CalcImpl calc = new CalcImpl();
        Registry registry = LocateRegistry.getRegistry(8878);
        System.out.println(registry);
        LocateRegistry.createRegistry(9898);
        registry = LocateRegistry.getRegistry(9898);
        System.out.println(registry.list().length);
        Naming.rebind("rmi://127.0.0.1:9898/calc", calc);

        ICalc a = (ICalc) Naming.lookup("rmi://127.0.0.1:9898/calc");
        System.out.println(a);
        ICalc b = (ICalc) Naming.lookup("rmi://192.168.56.1:9898/calc");
        System.out.println(b);
        System.out.println(a.equals(b));
    }
}
