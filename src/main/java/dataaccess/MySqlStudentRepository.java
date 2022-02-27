package dataaccess;

import domain.Course;
import domain.Student;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
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

        String sql = "Select * from `students`";

        try {
            PreparedStatement preparedStatement = con.prepareStatement(sql);
            ResultSet resultSet = preparedStatement.executeQuery();
            ArrayList<Student> studentList = new ArrayList<>();
            while (resultSet.next())
            {
                    studentList.add(new Student(
                            resultSet.getLong("id"),
                            resultSet.getString("studentVorname"),
                            resultSet.getString("studentNachname"),
                            resultSet.getDate("studentGb")
                    ));

            }return studentList;
        }catch (SQLException e){

            throw new DatabaseException("Database error occured!");
        }
    }

    @Override
    public Optional<Student> update(Student entity) {
        return Optional.empty();
    }

    @Override
    public void deleteById(Long id) {

    }
}
