/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package planeswalkers.simulatheure.data;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import planeswalkers.simulatheure.data.networkelement.NetworkElementFactory;
import planeswalkers.simulatheure.data.transit.TransitFactory;

/**
 *
 * @author Marc-Antoine Fortier
 */
public class NetworkFactory {

    private final HashMap<String, NetworkElementFactory> networkFactories;
    private final HashMap<String, TransitFactory> transitFactories;

    private NetworkFactory() {
        networkFactories = new HashMap<>();
        transitFactories = new HashMap<>();
    }

    /**
     * Return the only existing instance of the NetworkFactory.
     *
     * @return the only existing instance of the NetworkFactory.
     */
    public static NetworkFactory getInstance() {
        return NetworkFactoryHolder.INSTANCE;
    }

    /**
     * Creates a new NetworkElementFactory object if it doesn't exist. In any
     * case, it returns the instance of the existing one or of the newly
     * created.
     *
     * @param networkFactoryName the name of the NetowrkFactory to create or
     * retrieve.
     * @return the corresponding NetworkFactory instance.
     */
    public NetworkElementFactory getNetworkElementFactory(String networkFactoryName) {
        if (networkFactories.get(networkFactoryName) == null) {
            networkFactories.put(networkFactoryName, new NetworkElementFactory(networkFactoryName));
        }
        return networkFactories.get(networkFactoryName);
    }

    /**
     * Creates a new TransitFactory object if it doesn't exist. In any case, it
     * returns the instance of the existing one or of the newly created.
     *
     * @param transitFactoryName the name of the TransitFactory to create or
     * retrieve.
     * @return the corresponding TransitFactory instance.
     */
    public TransitFactory getTransitFactory(String transitFactoryName) {
        if (transitFactories.get(transitFactoryName) == null) {
            transitFactories.put(transitFactoryName, new TransitFactory(transitFactoryName));
        }
        return transitFactories.get(transitFactoryName);
    }

    private static class NetworkFactoryHolder {

        private static final NetworkFactory INSTANCE = new NetworkFactory();
    }

    public void save(String networkName, String path) {
        ObjectOutputStream oos = null;
        try {
            final FileOutputStream fichier = new FileOutputStream(path + ".ser");
            oos = new ObjectOutputStream(fichier);
            oos.writeObject(networkName);
            oos.writeObject(this.networkFactories.get(networkName));
            oos.writeObject(this.transitFactories.get(networkName));
        } catch (final IOException e) {
        } finally {
            try {
                if (oos != null) {
                    oos.flush();
                    oos.close();
                }
            } catch (final IOException e) {
            }
        }
    }

    public void load(String path) {
        ObjectInputStream ois = null;
        String networkName;
        try {
            final FileInputStream fichier = new FileInputStream(path);
            ois = new ObjectInputStream(fichier);
            networkName = (String) ois.readObject();
            networkFactories.put(networkName, (NetworkElementFactory) ois.readObject());
            transitFactories.put(networkName, (TransitFactory) ois.readObject());
        } catch (final IOException | ClassNotFoundException e) {
        } finally {
            try {
                ois.close();
            } catch (final IOException e) {
            }
        }
    }
}
