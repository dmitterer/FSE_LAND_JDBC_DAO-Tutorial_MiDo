package dataaccess;

import domain.Course;
import domain.Student;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class MySqlStudentRepository implements MyStudentRepository {

    private Connection con;

    public MySqlStudentRepository() throws SQLException, ClassNotFoundException {
        this.con = MysqlDatabaseConnection.getConnection("jdbc:mysql://localhost:3306/kurssystem", "root", "");
    }

    public List<Student> findAllStudentsByVorname(String studentVorname) {
        return null;
    }

    public List<Student> findAllStudentsByNachname(String studentNachname) {
        return null;
    }

    public List<Student> findAllStudentsByGbJahr(String searchGbJahr) {
        return null;
    }


    @Override
    public Optional<Student> insert(Student entity) {
        return Optional.empty();
    }

    @Override
    public Optional<Student> getById(Long id) {
        return Optional.empty();
    }

    @Override
    public List<Student> getAll() {
        return null;
    }

    @Override
    public Optional<Student> update(Student entity) {
        return Optional.empty();
    }

    @Override
    public void deleteById(Long id) {

    }
}
