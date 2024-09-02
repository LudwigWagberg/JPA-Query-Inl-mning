package se.yrgo.test;

import jakarta.persistence.*;

import se.yrgo.domain.Subject;
import se.yrgo.domain.Tutor;

import java.util.ArrayList;
import java.util.List;

public final class HibernateTest {
    private static final EntityManagerFactory emf;
    private static final EntityManager em;
    private static final EntityTransaction tx;

    static {
        emf = Persistence.createEntityManagerFactory("databaseConfig");
        em = emf.createEntityManager();
        tx = em.getTransaction();
    }

    public static void main(String[] args) {
        setUpData();
        taskOne();
        taskTwo();
        taskThree();
        taskFour();
        taskFive();
    }

    private static void setUpData() {
        try {
            tx.begin();
            Subject subject1 = new Subject("English Studies", 3);
            Subject subject2 = new Subject("Music", 3);
            Subject subject3 = new Subject("Social Studies", 2);
            Subject subject4 = new Subject("Economics", 1);
            Subject subject5 = new Subject("Mathematics", 4);
            Subject subject6 = new Subject("Physics", 3);

            Tutor tutor1 = new Tutor("PeFe", "Perry Ferry", 42000);
            Tutor tutor2 = new Tutor("MABR", "Maria Brown", 39500);
            Tutor tutor3 = new Tutor("PAJO", "Paul Johnson", 9800);
            Tutor tutor4 = new Tutor("CLDA", "Claire Davis", 8000);
            Tutor tutor5 = new Tutor("EMCL", "Emily Clark", 23900);
            Tutor tutor6 = new Tutor("ROSM", "Robert Smith", 34000);
            Tutor tutor7 = new Tutor("JANM", "Jane Miller", 27000);

            var tutors = new ArrayList<>(List.of(tutor1, tutor2, tutor3, tutor4, tutor5, tutor6, tutor7));
            tutors.forEach(em::persist);

            tutor1.addSubjectToTeach(subject1);
            tutor1.addSubjectToTeach(subject2);
            tutor1.addSubjectToTeach(subject3);
            tutor2.addSubjectToTeach(subject3);
            tutor3.addSubjectToTeach(subject1);
            tutor4.addSubjectToTeach(subject4);
            tutor5.addSubjectToTeach(subject3);
            tutor6.addSubjectToTeach(subject5);
            tutor7.addSubjectToTeach(subject6);

            tutor1.createStudentAndAddToTeachingGroup("Ethan Parker", "stu-25-7102", "Orchard Avenue 5", "Burbank",
                    "91507 CA");
            tutor1.createStudentAndAddToTeachingGroup("Sophia Mitchell", "stu-25-2147", "Willow Street 58 apt 15",
                    "Burbank", "91508 CA");
            tutor1.createStudentAndAddToTeachingGroup("Lucas Harper", "stu-25-3195", "Cedar Lane 330", "Burbank",
                    "91509 CA");
            tutor1.createStudentAndAddToTeachingGroup("Ava Thompson", "stu-25-0562", "King's Road 12", "Burbank",
                    "91511 CA");
            tutor2.createStudentAndAddToTeachingGroup("Isabella Garcia", "stu-25-4401", "Baker Street 123", "Burbank",
                    "92506 CA");
            tutor2.createStudentAndAddToTeachingGroup("James Miller", "stu-25-0836", "Elm Drive 45 apt 7", "Burbank",
                    "91512 CA");
            tutor3.createStudentAndAddToTeachingGroup("Lily Roberts", "stu-25-5093", "Pine Crest 89 apt 310", "Burbank",
                    "91906 CA");
            tutor4.createStudentAndAddToTeachingGroup("Henry Wilson", "stu-25-0527", "Summit Drive 102", "Burbank",
                    "91513 CA");
            tutor4.createStudentAndAddToTeachingGroup("Grace Edwards", "stu-25-8421", "Mountain Road 15", "Burbank",
                    "91514 CA");
            tutor5.createStudentAndAddToTeachingGroup("Olivia Brooks", "stu-25-7558", "Lakeview Avenue 78", "Burbank",
                    "91516 CA");
            tutor6.createStudentAndAddToTeachingGroup("Nathan Scott", "stu-25-9385", "River Road 33", "Burbank",
                    "91517 CA");
            tutor7.createStudentAndAddToTeachingGroup("Hannah Morgan", "stu-25-1247", "Forest Avenue 21", "Burbank",
                    "91518 CA");

        } catch (Exception ex) {
            System.err.println("Error: " + ex.getMessage());
            ex.printStackTrace();
        } finally {
            tx.commit();
        }
    }

    /***
     * Uppgift-1- Navigera över relationer(med member of)
     */
    private static void taskOne() {
        try {
            tx.begin();
            String subjectName = "Science"; // Ensure the case matches exactly with the stored data
            TypedQuery<Subject> fetchSubject = em.createNamedQuery("findSubjectByName", Subject.class)
                    .setParameter("name", subjectName);

            Subject science;
            try {
                science = fetchSubject.getSingleResult();
            } catch (NoResultException e) {
                System.out.println("No subject found with the name: " + subjectName);
                return;
            }

            var results = em.createQuery("select s.name from Tutor t join t.teachingGroup s" +
                    " where :subject member of t.subjectsToTeach", String.class)
                    .setParameter("subject", science).getResultList();
            System.out.println("------Result from task one------");
            results.forEach(System.out::println);

        } catch (Exception ex) {
            System.err.println("Error: " + ex.getMessage());
            ex.printStackTrace();
        } finally {
            tx.commit();
        }
    }

    /***
     * Uppgift-2- Report Query- Multiple fields (med join)
     */
    private static void taskTwo() {
        try {
            tx.begin();
            @SuppressWarnings("unchecked")
            List<Object[]> results = em.createQuery("select s.name, t.name from Tutor t join t.teachingGroup s")
                    .getResultList();

            System.out.println("------Result from task two------");
            for (Object[] pair : results) {
                System.out.println((results.indexOf(pair) + 1) + " Student: " + pair[0] + ", \t Tutor: " + pair[1]);
            }

        } catch (Exception ex) {
            System.err.println(ex.getMessage());
            ex.printStackTrace();
        } finally {
            tx.commit();
        }
    }

    /**
     * Uppgift-3-Report Query- Aggregation.
     */
    private static void taskThree() {
        try {
            tx.begin();

            double averageSemesterLength = (Double) em.createQuery("select avg(sub.numberOfSemesters) from Subject sub")
                    .getSingleResult();

            System.out.println("------Result from task three------");
            System.out.printf("Average semester length: %.2f%n%n", averageSemesterLength);

        } catch (Exception ex) {
            System.err.println(ex.getMessage());
            ex.printStackTrace();
        } finally {
            tx.commit();
        }
    }

    /***
     * Uppgift-4-Query med Aggregation
     */
    private static void taskFour() {
        try {
            tx.begin();
            var highestSalary = (Integer) em.createQuery("select max(t.salary) from Tutor t").getSingleResult();

            System.out.println("------Result from task four-----");
            System.out.printf("Highest salary for tutor: %d $%n%n", highestSalary);

        } catch (Exception ex) {
            System.err.println(ex.getMessage());
            ex.printStackTrace();
        } finally {
            tx.commit();
        }
    }

    /***
     * Uppgift-5- NamedQuery
     */
    private static void taskFive() {
        try {
            tx.begin();

            var tenThousand = 10000;
            List<Object[]> results = em.createNamedQuery("findTutorsWithSalaryAbove", Object[].class)
                    .setParameter("reqSalary", tenThousand)
                    .getResultList();

            System.out.println("------Result from task five-----");
            for (Object[] result : results) {
                System.out.printf("%s's salary: %d $%n", result[0], result[1]);
            }

        } catch (Exception ex) {
            System.err.println(ex.getMessage());
            ex.printStackTrace();
        } finally {
            tx.commit();
            em.close();
        }
    }
}