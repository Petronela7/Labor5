import controller.RegistrationSystem;

import repository.CourseRepository;
import repository.EnrolledRepository;
import repository.StudentRepository;
import repository.TeacherRepository;

import java.sql.*;

public class Main {
    public static void main(String[] args) throws SQLException {

        StudentRepository studentRepository = new StudentRepository();
        CourseRepository courseRepository = new CourseRepository();
        TeacherRepository teacherRepository = new TeacherRepository();
        EnrolledRepository enrolledRepository = new EnrolledRepository(courseRepository, studentRepository);
        RegistrationSystem registrationSystem = new RegistrationSystem(courseRepository, studentRepository,teacherRepository,enrolledRepository);

        System.out.println(studentRepository.findAll());
        System.out.println(courseRepository.findAll());
        System.out.println(teacherRepository.findAll());

        registrationSystem.register(studentRepository.findOne(100),courseRepository.findOne(1));
        registrationSystem.register(studentRepository.findOne(100),courseRepository.findOne(5));
        //registrationSystem.register(studentRepository.findOne(2),courseRepository.findOne(100));
        //System.out.println(registrationSystem.retrieveCoursesWithFreePlaces());
        //System.out.println(registrationSystem.retrieveStudentsFromCourse(courseRepository.findOne(100)));
        //System.out.println(registrationSystem.retrieveStudentsFromCourse(courseRepository.findOne(102)));
        //registrationSystem.updateCredits(courseRepository.findOne(100),2);
        //registrationSystem.deleteCourse(teacherRepository.findOne(1),courseRepository.findOne(102));
        //System.out.println(enrolledRepository.findCoursesByStudent(1));



    }
}
