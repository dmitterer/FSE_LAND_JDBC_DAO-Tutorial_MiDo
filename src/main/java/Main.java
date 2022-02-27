import dataaccess.MySqlCourseRepository;
import dataaccess.MySqlStudentRepository;
import dataaccess.MysqlDatabaseConnection;
import ui.Cli;

import javax.swing.*;
import java.sql.Connection;
import java.sql.SQLException;

public class Main {

    public static void main(String[] args) {


        try {
            Cli myCli = new Cli(new MySqlCourseRepository(),new MySqlStudentRepository());
            myCli.start();
        } catch (SQLException e) {
            System.out.println("Datenbankfehler: " + e.getMessage() + " SQL State: " + e.getSQLState());
        } catch (ClassNotFoundException e) {
            System.out.println("Datenbankfehler: " + e.getMessage());
        }


    }
}