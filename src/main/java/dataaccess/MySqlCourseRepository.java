package dataaccess;

import domain.Course;
import domain.CourseType;
import util.Assert;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class MySqlCourseRepository implements MyCourseRepository {


    private Connection con;

    public MySqlCourseRepository() throws SQLException, ClassNotFoundException {
        this.con = MysqlDatabaseConnection.getConnection("jdbc:mysql://localhost:3306/kurssystem", "root", "");
    }

    @Override
    public Optional<Course> insert(Course entity) {
        return Optional.empty();
    }

    @Override
    public Optional<Course> getById(Long id) {

        Assert.notNull(id);
        if (countCoursesInDbWithId(id)== 0) {


            return Optional.empty();
        } else
        {
            try {
                String sql = "Select * from `courses` where `id` = ?";
                PreparedStatement preparedStatement = con.prepareStatement(sql);
                preparedStatement.setLong(1,id);
                ResultSet resultSet = preparedStatement.executeQuery();

                resultSet.next();
                Course course = new Course(
                        resultSet.getLong("id"),
                        resultSet.getString("name"),
                        resultSet.getString("description"),
                        resultSet.getInt("hours"),
                        resultSet.getDate("begindate"),
                        resultSet.getDate("enddate"),
                        CourseType.valueOf(resultSet.getString("coursetype"))
                );

                return Optional.of(course);

            } catch (SQLException sqlException)

            {

                throw new DatabaseException(sqlException.getMessage());

            }
        }
    }

    private int countCoursesInDbWithId(Long id)
    {
        try {

        String countSql = "Select Count(*) from `courses` where `id` = ?";
        PreparedStatement preparedStatementCount = con.prepareStatement(countSql);
        preparedStatementCount.setLong(1, id);
        ResultSet resultSetCount = preparedStatementCount.executeQuery();
        resultSetCount.next();
        int courseCount = resultSetCount.getInt(1);
        return  courseCount;
        } catch (SQLException sqlException)
        {

            throw new DatabaseException(sqlException.getMessage());

        }

    }

    @Override
    public List<Course> getAll() {

        String sql = "Select * from `courses`";

        try {
            PreparedStatement preparedStatement = con.prepareStatement(sql);
            ResultSet resultSet = preparedStatement.executeQuery();
            ArrayList<Course> courseList = new ArrayList<>();
            while (resultSet.next()) {

                courseList.add(new Course(
                                resultSet.getLong("id"),
                                resultSet.getString("name"),
                                resultSet.getString("description"),
                                resultSet.getInt("hours"),
                                resultSet.getDate("begindate"),
                                resultSet.getDate("enddate"),
                                CourseType.valueOf(resultSet.getString("coursetype"))
                        )
                );


            }return courseList;

        } catch (SQLException e) {
            throw new DatabaseException("Database error occoured!");
        }


    }

    @Override
    public Optional<Course> update(Course entity) {
        return Optional.empty();
    }

    @Override
    public void deleteById(Long id) {

    }

    @Override
    public List<Course> findAllCoursesByName(String name) {
        return null;
    }

    @Override
    public List<Course> findAllCoursesByDescription(String description) {
        return null;
    }

    @Override
    public List<Course> findAllCoursesByNameOrDescription(String searchText) {
        return null;
    }

    @Override
    public List<Course> findAllCoursesByStartDate(Date startDate) {
        return null;
    }

    @Override
    public List<Course> findAllCoursesByCourseType(CourseType courseType) {
        return null;
    }

    @Override
    public List<Course> findAllRunningCourses() {
        return null;
    }
}
