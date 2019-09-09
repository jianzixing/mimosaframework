package org.mimosaframework.rmi;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ICalc extends Remote {
    long add(long n1, long n2) throws RemoteException;
}
