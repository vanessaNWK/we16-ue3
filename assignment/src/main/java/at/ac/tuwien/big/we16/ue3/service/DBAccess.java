package at.ac.tuwien.big.we16.ue3.service;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 * Created by vanessa on 18.05.16.
 */
public class DBAccess {
    private static EntityManagerFactory factory = Persistence.createEntityManagerFactory("defaultPersistenceUnit");
    private static EntityManager manager = factory.createEntityManager();

    private DBAccess() {}

    public static EntityManager getManager() {
        return manager;
    }


}
