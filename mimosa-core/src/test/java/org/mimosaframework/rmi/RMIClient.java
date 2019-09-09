package org.mimosaframework.rmi;

import javax.sql.DataSource;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

public class RMIClient {
    public static void main(String[] args) throws RemoteException, NotBoundException, MalformedURLException {
        ICalc calc1 = (ICalc) Naming.lookup("rmi://localhost:9898/calc");
        System.out.println(calc1.add(10, 25));

        ICalc calc2 = (ICalc) Naming.lookup("rmi://192.168.56.1:9898/calc_2");
        System.out.println(calc2.add(18, 25));

        String[] names = Naming.list("rmi://127.0.0.1:9898");
        for (String s : names) {
            System.out.println(s);
        }

        Naming.unbind("rmi://127.0.0.1:9898/calc_2");
    }
}
