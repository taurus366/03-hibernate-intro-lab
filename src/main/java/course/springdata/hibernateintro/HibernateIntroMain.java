package course.springdata.hibernateintro;

import course.springdata.hibernateintro.entity.Student;
import org.hibernate.*;
import org.hibernate.cfg.Configuration;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.stream.Collectors;

public class HibernateIntroMain {
    public static void main(String[] args) {
        // Create hibernate cfg
        Configuration cfg = new Configuration();
        cfg.configure();

        // Create session factory
        SessionFactory sf = cfg.buildSessionFactory();

        // Create Session
        Session session = sf.openSession();

        // Persist an entity
        Student student = new Student("H");
        session.beginTransaction();
        session.save(student);
        session.getTransaction().commit();

        // Read entity by Id
        session.beginTransaction();
        session.setHibernateFlushMode(FlushMode.MANUAL);
        Student result = session.get(Student.class, 5L, LockMode.READ);
//        Student result = session.byId(Student.class).load(1L);
        session.getTransaction().commit();
        System.out.printf("Student with ID:%d -> %s",result.getId(),result);

        session.beginTransaction();

        session.getTransaction().commit();

        // List of students using HQL
//        session.beginTransaction();
//        session.createQuery("FROM Student WHERE name like 'Ali%'",Student.class)
//                .setFirstResult(5)
//                .setMaxResults(5)
//                .stream()
//                .forEach(System.out::println);
//        ArrayList<Student> list = session.createQuery("FROM Student",Student.class)
//                .stream().collect(Collectors.toCollection(ArrayList::new));
//        session.getTransaction().commit();
//        System.out.println(list.size());

//        session.createQuery("FROM Student Where name = ?1",Student.class)
//                .setParameter(1, "H")
//                .stream().forEach(System.out::println);

        // Type-safe criteria quieries
        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery<Student> query = cb.createQuery(Student.class);
        Root<Student> Student_ = query.from(Student.class);
        query.select(Student_).where(cb.like(Student_.get("name"),"H%"));
        session.createQuery(query).getResultStream()
                .forEach(System.out::println);


        // Close session
        session.close();
    }
}
