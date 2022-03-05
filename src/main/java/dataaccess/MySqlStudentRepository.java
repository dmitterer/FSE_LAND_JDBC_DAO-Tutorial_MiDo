package dataaccess;

import domain.Course;
import domain.CourseType;
import domain.Student;
import util.Assert;

import java.sql.*;
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
    public List<Student> findAllStudentByVornameOrNachname(String searchText) {
        try {
            String sql = "SELECT * FROM `students` WHERE LOWER(`studentVorname`) LIKE LOWER(?) OR LOWER(`studentNachname`) LIKE LOWER(?)";
            PreparedStatement prepareStatement = con.prepareStatement(sql);
            prepareStatement.setString(1, "%" + searchText + "%");
            prepareStatement.setString(2, "%" + searchText + "%");
            ResultSet resultSet = prepareStatement.executeQuery();
            ArrayList<Student> studentList = new ArrayList<>();
            while (resultSet.next()) {
                studentList.add(new Student(
                        resultSet.getLong("id"),
                        resultSet.getString("studentVorname"),
                        resultSet.getString("studentNachname"),
                        resultSet.getDate("studentGb")));
            }
            return studentList;
        } catch (SQLException sqlException) {
            throw new DatabaseException(sqlException.getMessage());
        }
    }


    @Override
    public Optional<Student> insert(Student entity) {

        Assert.notNull(entity);

        try
        {

            String sql = "Insert into `students`  (`studentVorname`, `studentNachname`, `studentGb`) VALUES (?,?,?)";
            PreparedStatement preparedStatement = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, entity.getStudentVorname());
            preparedStatement.setString(2, entity.getStudentNachname());
            preparedStatement.setDate(3, entity.getStudentGb());

            int affectedRows = preparedStatement.executeUpdate();

            if (affectedRows == 0)
            {
                return Optional.empty();

            } else
            {
                ResultSet generatedKeys = preparedStatement.getGeneratedKeys();

                if (generatedKeys.next())
                {
                    return this.getById(generatedKeys.getLong(1));

                }else
                {
                    return Optional.empty();

                }
            }

        } catch (SQLException sqlException)
        {
            throw new DatabaseException(sqlException.getMessage());
        }
    }

    @Override
    public Optional<Student> getById(Long id) {

        Assert.notNull(id);
        if (countStudentsInDbWithId(id)== 0) {


            return Optional.empty();
        } else
        {
            try {
                String sql = "Select * from `students` where `id` = ?";
                PreparedStatement preparedStatement = con.prepareStatement(sql);
                preparedStatement.setLong(1,id);
                ResultSet resultSet = preparedStatement.executeQuery();

                resultSet.next();
                Student student = new Student(
                        resultSet.getLong("id"),
                        resultSet.getString("studentVorname"),
                        resultSet.getString("studentNachname"),
                        resultSet.getDate("studentGb")

                );

                return Optional.of(student);

            } catch (SQLException sqlException)

            {

                throw new DatabaseException(sqlException.getMessage());

            }
        }
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
    private int countStudentsInDbWithId(Long id)
    {
        try {

            String countSql = "Select Count(*) from `students` where `id` = ?";
            PreparedStatement preparedStatementCount = con.prepareStatement(countSql);
            preparedStatementCount.setLong(1, id);
            ResultSet resultSetCount = preparedStatementCount.executeQuery();
            resultSetCount.next();
            int studentCount = resultSetCount.getInt(1);
            return  studentCount;
        } catch (SQLException sqlException)
        {

            throw new DatabaseException(sqlException.getMessage());

        }

    }

    @Override
    public Optional<Student> update(Student entity) {
        Assert.notNull(entity);

        String sql = "UPDATE `students` SET `studentVorname` = ?, `studentNachname` = ?, `studentGb` = ? WHERE `students`.`id` = ?";


        if (countStudentsInDbWithId(entity.getId())==0)

        {
            return Optional.empty();
        } else
        {
            try
            {
                PreparedStatement preparedStatement = con.prepareStatement(sql);
                preparedStatement.setString(1, entity.getStudentVorname());
                preparedStatement.setString(2, entity.getStudentNachname());
                preparedStatement.setDate(3, entity.getStudentGb());
                preparedStatement.setLong(4, entity.getId());

                int affectedRows = preparedStatement.executeUpdate();

                if (affectedRows==0)
                {
                    return Optional.empty();
                }else
                {
                    return this.getById(entity.getId());
                }

            }catch (SQLException sqlException)
            {
                throw new DatabaseException(sqlException.getMessage());

            }
        }
    }

    @Override
    public void deleteById(Long id) {

        Assert.notNull(id);
        String sql = "Delete from `students` where `id` = ?";

        try {
            if (countStudentsInDbWithId(id) == 1) {

                PreparedStatement preparedStatement = con.prepareStatement(sql);
                preparedStatement.setLong(1, id);
                preparedStatement.executeUpdate();

            }
        } catch (SQLException sqlException)
        {
            throw new DatabaseException(sqlException.getMessage());
        }
    }
}
