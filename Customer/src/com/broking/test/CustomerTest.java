/**
 * 
 */
package com.broking.test;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;

import com.broking.dynamicproperties.approach.Customer;

/**
 * @author jpyla
 * 
 */
public class CustomerTest {

    private static final String PERSISTENCE_UNIT_NAME = "CustomerJPA";

    private static EntityManagerFactory factory;

    public static void main(String[] args) {
        factory = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME);
        EntityManager em = factory.createEntityManager();
         //Read the existing entries and write to console
         Query q = em.createQuery("SELECT u FROM Customer u");
         List<Customer> customerList = q.getResultList();
         for (Customer customer : customerList) {
         System.out.println(customer.getName());
         }
         System.out.println("Size: " + customerList.size());

        // Create new customer
        em.getTransaction().begin();
        Customer customer = new Customer();
        customer.setId("id2");
        customer.setName("Euler");
        customer.setCreatets(new Timestamp(System.currentTimeMillis()));
        customer.setUpdatets(new Timestamp(System.currentTimeMillis()));
        Calendar cl = new GregorianCalendar(1985, 8, 12);

        customer.setDob(new Date(cl.getTimeInMillis()));
        em.persist(customer);
        em.getTransaction().commit();

        em.close();
    }
}
