package org.mimosaframework.rmi;

import java.rmi.RemoteException;
import java.rmi.server.RMIClientSocketFactory;
import java.rmi.server.RMIServerSocketFactory;
import java.rmi.server.UnicastRemoteObject;

public class CalcImpl extends UnicastRemoteObject implements ICalc {


    public CalcImpl() throws RemoteException {
    }

    public CalcImpl(int port) throws RemoteException {
        super(port);
    }

    public CalcImpl(int port, RMIClientSocketFactory csf, RMIServerSocketFactory ssf) throws RemoteException {
        super(port, csf, ssf);
    }

    @Override
    public long add(long n1, long n2) throws RemoteException {
        return n1 + n2;
    }
}
