package controller;

import org.junit.jupiter.api.Test;
import repository.CourseRepository;
import repository.EnrolledRepository;
import repository.StudentRepository;
import repository.TeacherRepository;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

class RegistrationSystemTest {
    StudentRepository studentRepository = new StudentRepository();
    CourseRepository courseRepository = new CourseRepository();
    TeacherRepository teacherRepository = new TeacherRepository();
    EnrolledRepository enrolledRepository = new EnrolledRepository(courseRepository, studentRepository);
    RegistrationSystem registrationSystem = new RegistrationSystem(courseRepository, studentRepository,teacherRepository,enrolledRepository);

    RegistrationSystemTest() throws SQLException {
    }

    @Test
    void register() throws SQLException {
        assertTrue(registrationSystem.register(studentRepository.findOne(100), courseRepository.findOne(1)));
        assertFalse(registrationSystem.register(studentRepository.findOne(100), courseRepository.findOne(5)));//the number of credits exceeds the limit
        assertTrue(registrationSystem.register(studentRepository.findOne(101), courseRepository.findOne(2)));
        assertFalse(registrationSystem.register(studentRepository.findOne(102), courseRepository.findOne(6)));//testing with a course that doesn't exist
        registrationSystem.getEnrolledRepository().deleteAllEntries();
        registrationSystem.getStudentRepository().setAllCreditsTo0();

    }

    @Test
    void retrieveCoursesWithFreePlaces() throws SQLException {
        registrationSystem.register(studentRepository.findOne(100), courseRepository.findOne(1));
        registrationSystem.register(studentRepository.findOne(102), courseRepository.findOne(5));
        assertEquals(registrationSystem.retrieveCoursesWithFreePlaces().get(0).getCourseName(), "Algebra");
        assertEquals(registrationSystem.retrieveCoursesWithFreePlaces().get(1).getCourseName(), "Logic");
        assertEquals(registrationSystem.retrieveCoursesWithFreePlaces().get(2).getCourseName(), "English");
        assertEquals(registrationSystem.retrieveCoursesWithFreePlaces().get(3).getCourseName(), "German");
        assertEquals(registrationSystem.retrieveCoursesWithFreePlaces().size(), 4);
        registrationSystem.getEnrolledRepository().deleteAllEntries();
        registrationSystem.getStudentRepository().setAllCreditsTo0();
    }

    @Test
    void retrieveStudentsFromCourse() throws SQLException {
        registrationSystem.register(studentRepository.findOne(100), courseRepository.findOne(2));
        registrationSystem.register(studentRepository.findOne(102), courseRepository.findOne(5));
        registrationSystem.register(studentRepository.findOne(102), courseRepository.findOne(2));
        registrationSystem.register(studentRepository.findOne(101), courseRepository.findOne(4));
        assertEquals(registrationSystem.retrieveStudentsFromCourse(courseRepository.findOne(1)).size(), 0);
        assertEquals(registrationSystem.retrieveStudentsFromCourse(courseRepository.findOne(2)).size(), 2);
        assertEquals(registrationSystem.retrieveStudentsFromCourse(courseRepository.findOne(3)).size(), 0);
        assertEquals(registrationSystem.retrieveStudentsFromCourse(courseRepository.findOne(4)).size(), 1);
        assertEquals(registrationSystem.retrieveStudentsFromCourse(courseRepository.findOne(5)).size(), 1);
        registrationSystem.getEnrolledRepository().deleteAllEntries();
        registrationSystem.getStudentRepository().setAllCreditsTo0();

    }

    @Test
    void getAllCourses() throws SQLException {
        assertEquals(registrationSystem.getAllCourses().size(),5);
        assertEquals(registrationSystem.getAllCourses().get(0).getCourseName(),"Databases");
    }

    @Test
    void updateCredits() throws SQLException {
        registrationSystem.register(studentRepository.findOne(100), courseRepository.findOne(2));
        registrationSystem.register(studentRepository.findOne(102), courseRepository.findOne(5));
        registrationSystem.register(studentRepository.findOne(102), courseRepository.findOne(2));
        registrationSystem.register(studentRepository.findOne(101), courseRepository.findOne(4));
        registrationSystem.updateCredits(courseRepository.findOne(4),6);
        registrationSystem.updateCredits(courseRepository.findOne(2), 4);
        assertEquals(studentRepository.findOne(101).getTotalCredits(),6);
        assertEquals(studentRepository.findOne(102).getTotalCredits(),24);
        assertEquals(studentRepository.findOne(100).getTotalCredits(),4);
        registrationSystem.getEnrolledRepository().deleteAllEntries();
        registrationSystem.getStudentRepository().setAllCreditsTo0();

    }

    @Test
    void deleteCourse() throws SQLException {
        registrationSystem.register(studentRepository.findOne(100), courseRepository.findOne(2));
        registrationSystem.register(studentRepository.findOne(102), courseRepository.findOne(5));
        registrationSystem.register(studentRepository.findOne(102), courseRepository.findOne(2));
        registrationSystem.register(studentRepository.findOne(101), courseRepository.findOne(4));
        registrationSystem.register(studentRepository.findOne(101), courseRepository.findOne(2));

        registrationSystem.deleteCourse(teacherRepository.findOne(1000),courseRepository.findOne(2));
        assertEquals(enrolledRepository.findCoursesByStudent(studentRepository.findOne(102).getStudentID()).size(), 1);
        assertEquals(enrolledRepository.findCoursesByStudent(studentRepository.findOne(102).getStudentID()).get(0).getCourseName(),"German");
        assertEquals(enrolledRepository.findCoursesByStudent(studentRepository.findOne(100).getStudentID()).size(),0);
        assertEquals(enrolledRepository.findCoursesByStudent(studentRepository.findOne(101).getStudentID()).size(),1);


    }
}

