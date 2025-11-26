package hibernate;

import hibernate.Persona;
import hibernate.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;

public class Main {

	public static void main(String[] args) {
		
		Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction transaction = session.beginTransaction();

        Persona p = new Persona("Juan", 30);
        session.save(p);   // INSERT

        transaction.commit();
        session.close();
        HibernateUtil.shutdown();

	}

}
