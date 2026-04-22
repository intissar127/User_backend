package com.soutenancia.backend.controller;

import com.soutenancia.backend.DTO.AdminRequest;
import com.soutenancia.backend.DTO.StudentRequest;
import com.soutenancia.backend.DTO.TeacherRequest;
import com.soutenancia.backend.models.Role;
import com.soutenancia.backend.models.Student;
import com.soutenancia.backend.models.Teacher;
import com.soutenancia.backend.models.User;
import com.soutenancia.backend.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    // ─── USER (Admin) ───────────────────────────────────────────

    @GetMapping
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        return userService.getUserById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/email/{email}")
    public ResponseEntity<User> getUserByEmail(@PathVariable String email) {
        return userService.getUserByEmail(email)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/admin")
public User createAdmin(@RequestBody AdminRequest request) {
    User user = new User();
    user.setName(request.getName());
    user.setEmail(request.getEmail());
    user.setRole(Role.ADMIN);
    return userService.createAdmin(user, request.getPasswd());
}
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    // ─── STUDENT ────────────────────────────────────────────────

    @GetMapping("/students")
    public List<Student> getAllStudents() {
        return userService.getAllStudents();
    }

    @GetMapping("/students/{id}")
    public ResponseEntity<Student> getStudentById(@PathVariable Long id) {
        return userService.getStudentById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/students")
public Student createStudent(@RequestBody StudentRequest request) {
    Student student = new Student();
    student.setName(request.getName());
    student.setEmail(request.getEmail());
    student.setRole(Role.STUDENT);
    student.setSpecialite(request.getSpecialite());
    student.setFiliere(request.getFiliere());
    return userService.createStudent(student, request.getPasswd());
}

    @PutMapping("/students/{id}")
    public ResponseEntity<Student> updateStudent(@PathVariable Long id,
                                                  @RequestBody Student student) {
        return ResponseEntity.ok(userService.updateStudent(id, student));
    }

    // ─── TEACHER ────────────────────────────────────────────────

    @GetMapping("/teachers")
    public List<Teacher> getAllTeachers() {
        return userService.getAllTeachers();
    }

    @GetMapping("/teachers/{id}")
    public ResponseEntity<Teacher> getTeacherById(@PathVariable Long id) {
        return userService.getTeacherById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/teachers")
public Teacher createTeacher(@RequestBody TeacherRequest request) {
    Teacher teacher = new Teacher();
    teacher.setName(request.getName());
    teacher.setEmail(request.getEmail());
    teacher.setRole(Role.TEACHER);
    teacher.setSpecialite(request.getSpecialite());
    return userService.createTeacher(teacher, request.getPasswd());
}

    @PutMapping("/teachers/{id}")
    public ResponseEntity<Teacher> updateTeacher(@PathVariable Long id,
                                                  @RequestBody Teacher teacher) {
        return ResponseEntity.ok(userService.updateTeacher(id, teacher));
    }
}