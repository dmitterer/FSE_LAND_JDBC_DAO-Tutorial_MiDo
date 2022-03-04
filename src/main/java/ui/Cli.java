package ui;

import dataaccess.DatabaseException;
import dataaccess.MyCourseRepository;
import dataaccess.MySqlCourseRepository;
import dataaccess.MySqlStudentRepository;
import domain.Course;
import domain.CourseType;
import domain.InvalidValueException;
import domain.Student;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class Cli {

    Scanner scan;
    MySqlCourseRepository repo;
    MySqlStudentRepository repoStudent;

    public Cli(MySqlCourseRepository repo, MySqlStudentRepository repoStudent) {
        this.scan = new Scanner(System.in);
        this.repo = repo;
        this.repoStudent = repoStudent;

    }

    public void start() {

        String input = "-";
        while (!input.equals("x")) {

            showMenue();
            input = scan.nextLine();

            switch (input) {
                case "1":
                    addCourse();
                    break;

                case "2":
                    showAllCourses();
                    break;

                case "3":
                    showCourseDetails();
                    break;
                case "4":
                    updateCourseDetails();
                    break;
                case "5":
                    deleteCourse();
                    break;
                case "6":
                    courseSearch();
                    break;
                case "7":
                    runningCourses();
                    break;
                case "8":
                    showAllStudents();
                    break;
                case "9":
                    addStudent();
                    break;
                case "10":
                    updateStudentDetails();
                    break;
                case "x":
                    System.out.println("Auf Wiedersehen!");
                    break;
                default:
                    inputError();
                    break;
            }
        }
        scan.close();

    }
    private void updateStudentDetails() {

        System.out.println("Für welche Studenten-Id möchten Sie die Studenten ändern?");
        Long studentId = Long.parseLong(scan.nextLine());

        try {
            Optional<Student> optionalStudent = repoStudent.getById(studentId);
            if (optionalStudent.isEmpty()) {
                System.out.println("Student mit der gegebenen Id[" + studentId + "] nicht in der Datenbank!");
            } else {
                Student student = optionalStudent.get();

                System.out.println("Änderungen für folgenden Studenten: ");
                System.out.println(student);

                String studentVorname, studentNachname, studentGb;

                System.out.println("Bitte neue Studenten-daten angeben (Enter, falls keine Änderung gewüscht ist): ");
                System.out.println("Student Vorname: ");
                studentVorname = scan.nextLine();
                System.out.println("Student Nachname: ");
                studentNachname = scan.nextLine();
                System.out.println("Student Nachname: ");
                studentGb = scan.nextLine();

                Optional<Student> optionalStudentUpdated = repoStudent.update(

                        new Student(
                                student.getId(),
                                studentVorname.equals("") ? student.getStudentVorname() : studentVorname,
                                studentNachname.equals("") ? student.getStudentNachname() : studentNachname,
                                studentGb.equals("") ? student.getStudentGb() : Date.valueOf(studentGb)
                        )
                );

                optionalStudentUpdated.ifPresentOrElse(

                        (c) -> System.out.println("Student aktualisiert: " + c),
                        () -> System.out.println("Student konnte nicht aktualisiert werden!")
                );
            }

        } catch (IllegalArgumentException illegalArgumentException) {
            System.out.println("Eingabefehler: " + illegalArgumentException.getMessage());
        } catch (InvalidValueException invalidValueException) {
            System.out.println("Student nicht korrekt angegeben: " + invalidValueException.getMessage());
        } catch (DatabaseException databaseException) {
            System.out.println("Datenbankfehler beim Einfügen: " + databaseException.getMessage());
        } catch (Exception exception) {
            System.out.println("Unbekannter Fehler beim Einfügen: " + exception.getMessage());
        }
    }

    private void addStudent() {
        String studentVorname, studentNachname;
        Date bday;


        try {
            System.out.println("Bitte alle Studentendaten angeben:");
            System.out.println("Student Vorname: ");
            studentVorname = scan.nextLine();
            if (studentVorname.equals("")) throw new IllegalArgumentException("Eingabe darf nicht leer sein!");
            System.out.println("Student Nachname: ");
            studentNachname = scan.nextLine();
            if (studentNachname.equals("")) throw new IllegalArgumentException("Eingabe darf nicht leer sein!");
            System.out.println("Geburstdatum eingeben (YYYY-MM-DD): ");
            bday = Date.valueOf(scan.nextLine());


            Optional<Student> optionalStudent = repoStudent.insert(
                    new Student(studentVorname, studentNachname, bday)
            );

            if (optionalStudent.isPresent()) {
                System.out.println("Student angelegt: " + optionalStudent.get());

            } else {
                System.out.println("Student konnte nicht angelegt werden!");
            }

        } catch (IllegalArgumentException illegalArgumentException) {
            System.out.println("Eingabefehler: " + illegalArgumentException.getMessage());
        } catch (InvalidValueException invalidValueException) {
            System.out.println("Student wurde nicht korrekt angegeben: " + invalidValueException.getMessage());
        } catch (DatabaseException databaseException) {
            System.out.println("Datenbankfehler beim Einfügen: " + databaseException.getMessage());
        } catch (Exception exception) {
            System.out.println("Unbekannter Fehler beim Einfügen: " + exception.getMessage());
        }
    }

    private void showAllStudents() {
        List<Student> list = null;


        try {


            list = repoStudent.getAll();

            if (list.size() > 0) {
                for (Student student : list) {
                    System.out.println(student);

                }
            } else {
                System.out.println("Studentenliste Leer!");
            }
        } catch (DatabaseException databaseException) {

            System.out.println("Datenbankfehler bei Anzeige aller Studenten " + databaseException.getMessage());

        } catch (Exception exception) {

            System.out.println("Unbekannter Fehler bei Anzeige aller Studenten: " + exception.getMessage());

        }
    }


    private void runningCourses() {

        System.out.println("Aktuell laufende Kurse:");
        List<Course> list;

        try {

            list = repo.findAllRunningCourses();
            for (Course course : list)
            {
                System.out.println(course);
            }

        }catch (DatabaseException databaseException)
        {
            System.out.println("Datenbankfehler bei Kurs-Anzeige für laufende Kurse: " + databaseException.getMessage());
        }catch (Exception exception)
        {
            System.out.println("Unbekannter Fehler bei Kurs-Anzeig für laufende Kurse: " + exception.getMessage());
        }


    }

    private void courseSearch() {

        System.out.println("Geben Sie einen Suchbegriff an! ");
        String searchString = scan.nextLine();
        List<Course> courseList;

        try {

            courseList = repo.findAllCoursesByNameOrDescription(searchString);
            for (Course course : courseList)
            {
                System.out.println(course);
            }

        }catch (DatabaseException databaseException)
        {
            System.out.println("Datenbankfehler bei der Kurssuche: " + databaseException.getMessage());
        }catch (Exception exception)
        {
            System.out.println("Unbekannterfehler bei der Kurssuche: " + exception.getMessage());
        }



    }

    private void deleteCourse() {

        System.out.println("Welchen Kurs möchten Sie löschen? Bitte Id eingeben:");
        Long courseIdToDelete = Long.parseLong(scan.nextLine());

        try {
            repo.deleteById(courseIdToDelete);
            System.out.println("Kurs mit Id" + courseIdToDelete + " wurde gelöscht!");
        } catch (DatabaseException databaseException) {
            System.out.println("Datenbankfehler beim Löschen: " + databaseException.getMessage());
        } catch (Exception e) {
            System.out.println("Unbekannter Fehler beim Löschen: " + e.getMessage());
        }
    }

    private void updateCourseDetails() {

        System.out.println("Für welche Kurs-Id möchten Sie die Kursdetails ändern?");
        Long courseId = Long.parseLong(scan.nextLine());

        try {
            Optional<Course> courseOptional = repo.getById(courseId);
            if (courseOptional.isEmpty()) {
                System.out.println("Kurs mit der gegebenen Id nicht in der Datenbank!");
            } else {
                Course course = courseOptional.get();

                System.out.println("Änderungen für folgenden Kurs: ");
                System.out.println(course);

                String name, description, hours, dateFrom, dateTo, courseType;

                System.out.println("Bitte neue Kursdaten angeben (Enter, falls keine Änderung gewüscht ist): ");
                System.out.println("Name: ");
                name = scan.nextLine();
                System.out.println("Beschreibung: ");
                description = scan.nextLine();
                System.out.println("Stundenanzahl: ");
                hours = scan.nextLine();
                System.out.println("Startdatum (YYYY-MM-DD): ");
                dateFrom = scan.nextLine();
                System.out.println("Enddatum (YYYY-MM-DD): ");
                dateTo = scan.nextLine();
                System.out.println("Kurstyp (ZA/BF/FF/OE): ");
                courseType = scan.nextLine();

                Optional<Course> optionalCourseUpdated = repo.update(

                        new Course(
                                course.getId(),
                                name.equals("") ? course.getName() : name,
                                description.equals("") ? course.getDescription() : description,
                                hours.equals("") ? course.getHours() : Integer.parseInt(hours),
                                dateFrom.equals("") ? course.getBeginDate() : Date.valueOf(dateFrom),
                                dateTo.equals("") ? course.getEndDate() : Date.valueOf(dateTo),
                                courseType.equals("") ? course.getCourseType() : CourseType.valueOf(courseType)
                        )
                );

                optionalCourseUpdated.ifPresentOrElse(

                        (c) -> System.out.println("Kurs aktualisiert: " + c),
                        () -> System.out.println("Kurs konnte nicht aktualisiert werden!")
                );
            }

        } catch (IllegalArgumentException illegalArgumentException) {
            System.out.println("Eingabefehler: " + illegalArgumentException.getMessage());
        } catch (InvalidValueException invalidValueException) {
            System.out.println("Kurs nicht korrekt angegeben: " + invalidValueException.getMessage());
        } catch (DatabaseException databaseException) {
            System.out.println("Datenbankfehler beim Einfügen: " + databaseException.getMessage());
        } catch (Exception exception) {
            System.out.println("Unbekannter Fehler beim Einfügen: " + exception.getMessage());
        }
    }

    private void addCourse() {

        String name, description;
        int hours;
        Date dateFrom, dateTo;
        CourseType courseType;

        try {
            System.out.println("Bitte alle Kursdaten angeben:");
            System.out.println("Name: ");
            name = scan.nextLine();
            if (name.equals("")) throw new IllegalArgumentException("Eingabe darf nicht leer sein!");
            System.out.println("Beschreibung: ");
            description = scan.nextLine();
            if (description.equals("")) throw new IllegalArgumentException("Eingabe darf nicht leer sein!");
            System.out.println("Stundenanzahl: ");
            hours = Integer.parseInt(scan.nextLine());
            System.out.println("Startdatum (YYYY-MM-DD): ");
            dateFrom = Date.valueOf(scan.nextLine());
            System.out.println("Enddatum (YYYY-MM-DD): ");
            dateTo = Date.valueOf(scan.nextLine());
            System.out.println("Kurstyp (ZA/BF/FF/OE): ");
            courseType = CourseType.valueOf(scan.nextLine());

            Optional<Course> optionalCourse = repo.insert(
                    new Course(name, description, hours, dateFrom, dateTo, courseType)
            );

            if (optionalCourse.isPresent()) {
                System.out.println("Kurs angelegt: " + optionalCourse.get());

            } else {
                System.out.println("Kurs konnte nicht angelegt werden!");
            }

        } catch (IllegalArgumentException illegalArgumentException) {
            System.out.println("Eingabefehler: " + illegalArgumentException.getMessage());
        } catch (InvalidValueException invalidValueException) {
            System.out.println("Kurs nicht korrekt angegeben: " + invalidValueException.getMessage());
        } catch (DatabaseException databaseException) {
            System.out.println("Datenbankfehler beim Einfügen: " + databaseException.getMessage());
        } catch (Exception exception) {
            System.out.println("Unbekannter Fehler beim Einfügen: " + exception.getMessage());
        }
    }

    private void showCourseDetails() {

        System.out.println("Für welchen Kurs möchten Sie die Kursdetails anzeigen?");
        Long courseId = Long.parseLong(scan.nextLine());
        try {

            Optional<Course> courseOptional = repo.getById(courseId);

            if (courseOptional.isPresent()) {

                System.out.println(courseOptional.get());

            } else {

                System.out.println("Kurs mit der ID " + courseId + " nicht gefunden!");
            }


        } catch (DatabaseException databaseException) {

            System.out.println("Datenbankfehler bei Kurs-Detailanzeige: " + databaseException.getMessage());

        } catch (Exception exception) {

            System.out.println("Unbekannter Fehler bei Kurs-Detailanzeige: " + exception.getMessage());

        }

    }

    private void showAllCourses() {
        List<Course> list = null;


        try {


            list = repo.getAll();

            if (list.size() > 0) {
                for (Course course : list) {
                    System.out.println(course);

                }
            } else {
                System.out.println("Kursliste Leer!");
            }
        } catch (DatabaseException databaseException) {

            System.out.println("Datenbankfehler bei Anzeige aller Kurse " + databaseException.getMessage());

        } catch (Exception exception) {

            System.out.println("Unbekannter Fehler bei Anzeige aller Kurse: " + exception.getMessage());

        }
    }

    private void showMenue() {
        System.out.println("---------------- Kurs- und Studentmanagement -----------------");
        System.out.println("(1) Kurs eingeben \t (2) Alle Kurse anzeigen \t " + "(3) Kursdetails anzeigen");
        System.out.println("(4) Kursdetails ändern \t (5) Kurslöschen \t (6) Kurssuche\t (7) Laufende Kurse");
        System.out.println("(8) Alle Studenten anzeigen \t (9) Student anlegen \t (10) Studentdetails ändern\t");
        System.out.println("(x) Ende");


    }

    private void inputError() {
        System.out.println("Bitte nur die Zahlen der Menüauswahl eingeben!");
    }

}
