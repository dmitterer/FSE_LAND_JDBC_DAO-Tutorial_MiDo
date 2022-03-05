package dataaccess;

import domain.Student;

import java.util.List;
import java.sql.Date;

public interface MyStudentRepository extends BaseRepository<Student, Long>
{
    List<Student> findAllStudentsByVorname(String studentVorname);
    List<Student> findAllStudentsByNachname(String studentNachname);
    List<Student> findAllStudentsByGbJahr(String searchGbJahr);
    List<Student> findAllStudentByVornameOrNachname(String searchText);
    List<Student>findAllStudentsByGb(Date studentGb);
}
