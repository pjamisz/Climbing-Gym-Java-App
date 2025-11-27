package org.example;


import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public class Main {

	private static SessionFactory sessionFactory;

	public static void main(String[] args) {

		//Establish connection to database
		try (Connection connection = DriverManager.getConnection("jdbc:h2:mem:")){
			System.out.println("Valid connection: "+connection.isValid(0));
		}
		catch(SQLException e){
			e.printStackTrace();

		}
		//Configure the database according to hibernate config file
		sessionFactory = new Configuration()
				.configure("hibernate.cfg.xml")
				.buildSessionFactory();



		//Create gyms and climbers
		Climber climber1 = new Climber("Michał", LocalDate.now() );
		Climber climber2 = new Climber("Ewelina", LocalDate.now());

		Gym gym1 = new Gym("Murall");
		Gym gym2 = new Gym("Crux");

		Instructor instructor = new Instructor("Krzystof Korabik");
		ClimbingLesson individual = new IndividualLesson(instructor, 60, LocalDateTime.now(), climber1);

		//Create memberships with associations
		Membership membership1 = new Membership(climber1,gym1, LocalDate.now().plusDays(30));
		Membership membership2 = new Membership(climber2,gym2, LocalDate.now().plusDays(60));

		//Create authors
		Author adam = new Author("Adam Ondra");
		Author andrzej = new Author("Andrzej Mechanior");
		Author chris = new Author("Krzystof Szeroki");
		Author ewel = new Author("Ewelina Kucharska");
		Author tomasz = new Author("Tomasz Comasz");

		// Create routes and restrictions and link the associations
		Route route1 = new Route("Silence", 10, LocalDate.now());
		route1.createRestriction("Extreme overhang");
		route1.createRestriction("Tiny crimps");
		route1.setAuthor(adam);

		Route route2 = new Route("Trudna droga po zielonych", 8,LocalDate.now().minusDays(20));
		route2.createRestriction("Nie dotykać kantu");
		route2.setAuthor(adam);

		Route route3 = new Route("Freerider", 7,LocalDate.now());
		route3.createRestriction("3000ft of climbing");
		route3.createRestriction("Multiple pitches");
		route3.setAuthor(andrzej);


		Route route4 = new Route("Droga Kursowa", 1,LocalDate.now().minusDays(10));
		route4.createRestriction("Splippery, be careful");
		route4.createRestriction("Reserved for lessons");
		route4.setAuthor(chris);

		Route route5 = new Route("Dawn Wall", 9, LocalDate.now().minusDays(40));
		route5.createRestriction("Experience requiered");
		route5.setAuthor(tomasz);


		Session session = sessionFactory.openSession();

		session.beginTransaction();
		session.save(instructor);
		session.save(individual);
		session.save(climber1);
		session.save(climber2);
		session.save(gym1);
		session.save(gym2);
		session.save(membership1);
		session.save(membership2);
		session.save(adam);
		session.save(andrzej);
		session.save(chris);
		session.save(ewel);
		session.save(tomasz);

		session.save(route1);
		session.save(route2);
		session.save(route3);
		session.save(route4);
		session.save(route5);

		session.getTransaction().commit();


		//Example of persistency in H2 database
		session = sessionFactory.openSession();
		session.beginTransaction();
		List result = session.createQuery("from Climber").list();
		System.out.println("Climbers from query: ");
		for (Climber event : (List<Climber>) result) {
			System.out.println(event.getName());
		}
		result = session.createQuery("from Membership").list();
		System.out.println("Memberships from query: ");
		for (Membership event : (List<Membership>) result) {
			System.out.println(event.getClimberName());
			System.out.println(event.getGymName());
			System.out.println(event.getValidityDate());
		}
		session.getTransaction().commit();
		session.close();

		//Main GUI application

		ClimbingRoutesGUI gui = new ClimbingRoutesGUI(sessionFactory);
		gui.setVisible(true);
	}

}