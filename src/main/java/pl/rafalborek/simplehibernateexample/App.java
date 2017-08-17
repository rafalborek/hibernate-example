package pl.rafalborek.simplehibernateexample;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.resource.transaction.spi.TransactionStatus;

import pl.rafalborek.simplehibernateexample.dto.AddressDetails;
import pl.rafalborek.simplehibernateexample.dto.UserAccount;
import pl.rafalborek.simplehibernateexample.dto.UserAccountExtended;

/**
 * 
 * some examples of usage
 *
 */
public class App {
	public static void main(String[] args) {
		UserAccount user = new UserAccount();
		user.setUserName("Username 1");

		UserAccount user2 = new UserAccount();
		user2.setUserName("Username 2");

		List<AddressDetails> addresses = new ArrayList<>();

		for (int i = 0; i < 20; i++) {
			AddressDetails addressDetails = new AddressDetails("Address " + Integer.toString(i));
			addresses.add(addressDetails);
			if (i < 10) {
				// user.getAddresses().add(addressDetails);
				addressDetails.setUser(user);
			} else {
				// user2.getAddresses().add(addressDetails);
				addressDetails.setUser(user2);
			}
		}

		SessionFactory sessionFactory = new Configuration().configure().buildSessionFactory();

		Session session = sessionFactory.openSession();

		final Session s = session;
		session.beginTransaction();

		addresses.forEach(address -> s.save(address));

		session.save(user);
		session.save(user2);

		session.getTransaction().commit();
		session.close();

		System.out.println(user.getId());
		user.getAddresses().forEach(a -> System.out.println(a.getAddress()));
		user2.getAddresses().forEach(a -> System.out.println(a.getAddress()));

		session = sessionFactory.openSession();

		session.beginTransaction();
		user.getAddresses().addAll(addresses);
		user.getAddresses().forEach(a -> System.out.println(a.getAddress()));

		System.out.println("---Now---");
		user = session.get(UserAccount.class, user.getId());
		// user.getAddresses().forEach(a->System.out.println(a.getAddress()));
		user2 = session.get(UserAccount.class, user2.getId());
		user2.getAddresses().forEach(a -> System.out.println(a.getAddress()));

		if (session.getTransaction().getStatus().equals(TransactionStatus.ACTIVE))
			session.getTransaction().commit();
		session.close();

		// FetchType.LAZY
		// possible set to FetchType.EAGER
		// user.getAddresses().forEach(a->System.out.println(a.getAddress()));

		user2.getAddresses().forEach(a -> System.out.println(a.getAddress()));
		System.out.println(user.getId());
		System.out.println(user.getUserName());

		session = sessionFactory.openSession();
		session.beginTransaction();
		CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
		CriteriaQuery<AddressDetails> criteriaQuery = criteriaBuilder.createQuery(AddressDetails.class);
		Root<AddressDetails> addressRoot = criteriaQuery.from(AddressDetails.class);

		criteriaQuery.select(addressRoot).where(criteriaBuilder.equal(addressRoot.get("address"), "Address 3"));
		AddressDetails addr = session.createQuery(criteriaQuery).getSingleResult();
		System.out.println(addr.getAddress());

		if (session.getTransaction().getStatus().equals(TransactionStatus.ACTIVE))
			session.getTransaction().commit();
		session.close();

		System.out.println(addr.getAddress());
		System.out.println(addr.getUser().getId());
		// System.out.println(((List<AddressDetails>)
		// addr.getUser().getAddresses()).get(0));

		session = sessionFactory.openSession();
		session.beginTransaction();
		user = session.get(UserAccount.class, user.getId());
		session.delete(user);
		session.flush();

		user = session.get(UserAccount.class, user.getId());
		if (session.getTransaction().getStatus().equals(TransactionStatus.ACTIVE))
			session.getTransaction().commit();
		session.close();

		try {
			System.out.println(user.getUserName());
		} catch (NullPointerException e) {
			System.err.println("The user has been deleted");

		}
		
		List<UserAccount> userAccounts = new ArrayList<>();
		
		for(int i=0;i<10;i++) {
			UserAccount user3 = new UserAccount();
			user3.setUserName("test " + Integer.toString(10*i+1));
			userAccounts.add(user3);
			UserAccount user4 = new UserAccountExtended("test " + Integer.toString(i*2), "UserAccountExtended " + Integer.toString(i*2));
			userAccounts.add(user4);
		}

		session = sessionFactory.openSession();
		session.beginTransaction();
		for (UserAccount u : userAccounts) session.save(u);
		session.getTransaction().commit();
		session.close();
		
	}
}
