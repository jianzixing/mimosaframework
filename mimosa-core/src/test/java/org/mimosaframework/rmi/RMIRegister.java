package org.mimosaframework.rmi;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.RemoteException;

public class RMIRegister {
    public static void main(String[] args) throws RemoteException, MalformedURLException {
        CalcImpl calc = new CalcImpl();
        Naming.rebind("rmi://127.0.0.1:9898/calc", calc);
    }
}
