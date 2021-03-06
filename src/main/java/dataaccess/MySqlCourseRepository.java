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

        Assert.notNull(entity);

        try
        {

        String sql = "Insert into `courses`  (`name`, `description`, `hours`, `begindate`, `enddate`, `coursetype`) VALUES (?,?,?,?,?,?)";
        PreparedStatement preparedStatement = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
        preparedStatement.setString(1, entity.getName());
        preparedStatement.setString(2, entity.getDescription());
        preparedStatement.setInt(3, entity.getHours());
        preparedStatement.setDate(4, entity.getBeginDate());
        preparedStatement.setDate(5, entity.getEndDate());
        preparedStatement.setString(6, entity.getCourseType().toString());

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

        Assert.notNull(entity);

        String sql = "UPDATE `courses` SET `name` = ?, `description` = ?, `hours` = ?, `begindate` = ?, `enddate` = ?, `coursetype` = ? WHERE `courses`.`id` = ?";


        if (countCoursesInDbWithId(entity.getId())==0)

        {
            return Optional.empty();
        } else
        {
            try
            {
                PreparedStatement preparedStatement = con.prepareStatement(sql);
                preparedStatement.setString(1, entity.getName());
                preparedStatement.setString(2, entity.getDescription());
                preparedStatement.setInt(3, entity.getHours());
                preparedStatement.setDate(4, entity.getBeginDate());
                preparedStatement.setDate(5, entity.getEndDate());
                preparedStatement.setString(6, entity.getCourseType().toString());
                preparedStatement.setLong(7, entity.getId());

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
        String sql = "Delete from `courses` where `id` = ?";

        try {
            if (countCoursesInDbWithId(id) == 1) {

                PreparedStatement preparedStatement = con.prepareStatement(sql);
                preparedStatement.setLong(1, id);
                preparedStatement.executeUpdate();

            }
        } catch (SQLException sqlException)
        {
            throw new DatabaseException(sqlException.getMessage());
        }
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

        try {

            String sql = "Select *  from `courses`  where Lower(`description`) Like Lower(?) Or Lower (`name`) like Lower (?)";
            PreparedStatement preparedStatement = con.prepareStatement(sql);
            preparedStatement.setString(1, "%"+searchText+"%");
            preparedStatement.setString(2, "%"+searchText+"%");
            ResultSet resultSet = preparedStatement.executeQuery();
            ArrayList<Course> courseList = new ArrayList<>();
            while (resultSet.next())
            {

                courseList.add(new Course(
                        resultSet.getLong("id"),
                        resultSet.getString("name"),
                        resultSet.getString("description"),
                        resultSet.getInt("hours"),
                        resultSet.getDate("begindate"),
                        resultSet.getDate("enddate"),
                        CourseType.valueOf(resultSet.getString("coursetype")
                        )));

            }
            return courseList;



        }catch (SQLException sqlException)
        {
            throw new DatabaseException(sqlException.getMessage());

        }

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

        String sql = "Select * from `courses` where now()<`enddate`";
        try {

        PreparedStatement preparedStatement = con.prepareStatement(sql);
        ResultSet resultSet = preparedStatement.executeQuery();
        ArrayList<Course> courseList = new ArrayList<>();
        while (resultSet.next())
        {
                courseList.add(new Course(
                        resultSet.getLong("id"),
                        resultSet.getString("name"),
                        resultSet.getString("description"),
                        resultSet.getInt("hours"),
                        resultSet.getDate("begindate"),
                        resultSet.getDate("enddate"),
                        CourseType.valueOf(resultSet.getString("coursetype"))
                        ));
        }
            return courseList;
        }catch (SQLException sqlException)
        {
            throw new DatabaseException("Datenbankfehler: " + sqlException.getMessage());
        }

    }
}
