package domain;

import java.sql.*;

public class Student extends BaseEntity {

    private String studentVorname;
    private String studentNachname;
    private Date studentGb;

    public Student(Long id, String studentVorname, String studentNachname, Date studentGb) {
        super(id);
        this.setStudentVorname(studentVorname);
        this.setStudentNachname(studentNachname);
        this.setStudentGb(studentGb);

    }

    public Student(String studentVorname, String studentNachname, Date studentGb) {
        super(null);
        this.setStudentVorname(studentVorname);
        this.setStudentNachname(studentNachname);
        this.setStudentGb(studentGb);

    }

    public String getStudentVorname() {
        return studentVorname;
    }

    public void setStudentVorname(String studentVorname) throws InvalidValueException {

        if (studentVorname != null && studentVorname.length() > 1) {
            this.studentVorname = studentVorname;
        } else {
            throw new InvalidValueException("Vorname des Student muss mindestens 2 Zeichen lang sein!!");
        }
    }

    public String getStudentNachname() {
        return studentNachname;
    }

    public void setStudentNachname(String studentNachname) throws InvalidValueException {
        if (studentNachname != null && studentNachname.length() > 2) {
            this.studentNachname = studentNachname;
        } else {
            throw new InvalidValueException("Nachname des Student muss mindestens 2 Zeichen lang sein!!");
        }
    }

    public Date getStudentGb() {
        return studentGb;
    }

    public void setStudentGb(Date studentGb) {

        if (studentGb != null) {
            this.studentGb = studentGb;
        } else {
            throw new InvalidValueException("Das Geburtstdatum darf nicht null / leer sein!!");
        }
    }

    @Override
    public String toString() {
        return "Student{" +
                "id: " + this.getId() +
                ", studentVorname= '" + studentVorname + '\'' +
                ", studentNachname= '" + studentNachname + '\'' +
                ", studentGb= " + studentGb +
                '}';
    }
}
