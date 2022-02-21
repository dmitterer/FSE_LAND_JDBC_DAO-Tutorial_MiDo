package ui;

import dataaccess.DatabaseException;
import dataaccess.MyCourseRepository;
import dataaccess.MySqlCourseRepository;
import domain.Course;

import java.util.List;
import java.util.Scanner;

public class Cli {

    Scanner scan;
    MySqlCourseRepository repo;

    public Cli(MySqlCourseRepository repo)
    {
        this.scan = new Scanner(System.in);
        this.repo = repo;
    }

    public void start()
    {

    String input = "-";
    while (!input.equals("x"))

    {

        showMenue();
        input = scan.nextLine();

        switch (input) {
            case "1":
                System.out.println("Kurseingabe");
                break;

            case "2":
                showAllCourses();
                break;

            case "x":
                System.out.println("Auf Wiedersehen!");
                break;
            default:
                inputError();
                break;
        }
    }scan.close();

    }

    private void showAllCourses() {
        List<Course> list = null;


        try {


        list = repo.getAll();

        if (list.size()>0)
        {
            for (Course course : list)
            {
                System.out.println(course);

            }
        }else
        {
            System.out.println("Kursliste Leer!");
        }
        }catch (DatabaseException databaseException)
        {

            System.out.println("Datenbankfehler bei Anzeige aller Kurse " + databaseException.getMessage());

        }catch (Exception exception)
        {

            System.out.println("Unbekannter Fehler bei Anzeige aller Kurse: " + exception.getMessage());

        }
    }

    private void showMenue()

    {
        System.out.println("---------------- Kursmanagement -----------------");
        System.out.println("(1) Kurs eingeben \t (2) Alle Kurse anzeigen");
        System.out.println("(x) Ende");

    }

    private void inputError()
    {
        System.out.println("Bitte nur die Zahlen der Menüauswahl eingeben!");
    }

}
