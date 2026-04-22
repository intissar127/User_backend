// package com.soutenancia.backend.service;

// import com.soutenancia.backend.models.Student;
// import com.soutenancia.backend.models.Teacher;
// import com.soutenancia.backend.models.User;
// import com.soutenancia.backend.repository.UserRepository;
// import com.soutenancia.backend.repository.StudentRepository;
// import com.soutenancia.backend.repository.TeacherRepository;
// import org.springframework.stereotype.Service;
// import java.util.List;
// import java.util.Optional;

// @Service
// public class UserService {

//     private final UserRepository userRepository;
//     private final StudentRepository studentRepository;
//     private final TeacherRepository teacherRepository;
//     private final KeycloakService keycloakService;

//     public UserService(UserRepository userRepository,
//                        StudentRepository studentRepository,
//                        TeacherRepository teacherRepository,
//                        KeycloakService keycloakService) {
//         this.userRepository = userRepository;
//         this.studentRepository = studentRepository;
//         this.teacherRepository = teacherRepository;
//         this.keycloakService = keycloakService;
//     }

//     // ─── USER (Admin) ───────────────────────────────────────────

//     public List<User> getAllUsers() { return userRepository.findAll(); }

//     public Optional<User> getUserById(Long id) { return userRepository.findById(id); }

//     public Optional<User> getUserByEmail(String email) { return userRepository.findByEmail(email); }

//     public User createAdmin(User user, String password) {
//         User saved = userRepository.save(user);
//         keycloakService.createUser(user.getName(), user.getEmail(), password, "ADMIN");
//         return saved;
//     }

//     public void deleteUser(Long id) { userRepository.deleteById(id); }

//     // ─── STUDENT ────────────────────────────────────────────────

//     public List<Student> getAllStudents() { return studentRepository.findAll(); }

//     public Optional<Student> getStudentById(Long id) { return studentRepository.findById(id); }

//     public Student createStudent(Student student, String password) {
//         Student saved = studentRepository.save(student);
//         keycloakService.createUser(student.getName(), student.getEmail(), password, "STUDENT");
//         return saved;
//     }

//     public Student updateStudent(Long id, Student updatedStudent) {
//         return studentRepository.findById(id).map(student -> {
//             student.setName(updatedStudent.getName());
//             student.setEmail(updatedStudent.getEmail());
//             student.setRole(updatedStudent.getRole());
//             student.setSpecialite(updatedStudent.getSpecialite());
//             student.setFiliere(updatedStudent.getFiliere());
//             return studentRepository.save(student);
//         }).orElseThrow(() -> new RuntimeException("Student not found with id: " + id));
//     }

//     // ─── TEACHER ────────────────────────────────────────────────

//     public List<Teacher> getAllTeachers() { return teacherRepository.findAll(); }

//     public Optional<Teacher> getTeacherById(Long id) { return teacherRepository.findById(id); }

//     public Teacher createTeacher(Teacher teacher, String password) {
//         Teacher saved = teacherRepository.save(teacher);
//         keycloakService.createUser(teacher.getName(), teacher.getEmail(), password, "TEACHER");
//         return saved;
//     }

//     public Teacher updateTeacher(Long id, Teacher updatedTeacher) {
//         return teacherRepository.findById(id).map(teacher -> {
//             teacher.setName(updatedTeacher.getName());
//             teacher.setEmail(updatedTeacher.getEmail());
//             teacher.setRole(updatedTeacher.getRole());
//             teacher.setSpecialite(updatedTeacher.getSpecialite());
//             return teacherRepository.save(teacher);
//         }).orElseThrow(() -> new RuntimeException("Teacher not found with id: " + id));
//     }
// }
package com.soutenancia.backend.service;

import com.soutenancia.backend.models.Student;
import com.soutenancia.backend.models.Teacher;
import com.soutenancia.backend.models.User;
import com.soutenancia.backend.repository.UserRepository;
import com.soutenancia.backend.repository.StudentRepository;
import com.soutenancia.backend.repository.TeacherRepository;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    // ─── REPOSITORIES ───────────────────────────────────────────

    private final UserRepository userRepository;
    private final StudentRepository studentRepository;
    private final TeacherRepository teacherRepository;

    // ─── KEYCLOAK ───────────────────────────────────────────────

    private final Keycloak keycloak = KeycloakBuilder.builder()
            .serverUrl("http://localhost:8180")
            .realm("master")
            .clientId("admin-cli")
            .username("admin")
            .password("admin")
            .build();

    private final String REALM = "soutenancia";

    public UserService(UserRepository userRepository,
                       StudentRepository studentRepository,
                       TeacherRepository teacherRepository) {
        this.userRepository = userRepository;
        this.studentRepository = studentRepository;
        this.teacherRepository = teacherRepository;
    }

    // ═══════════════════════════════════════════════════════════
    // KEYCLOAK METHODS
    // ═══════════════════════════════════════════════════════════

    private void createKeycloakUser(String name, String email, String password, String role) {
        System.out.println("=== Creating Keycloak user: " + email + " with role: " + role);

        CredentialRepresentation credential = new CredentialRepresentation();
        credential.setType(CredentialRepresentation.PASSWORD);
        credential.setValue(password);
        credential.setTemporary(false);

        String username = name.toLowerCase().replace(" ", "_");
        System.out.println("=== username généré: " + username);

        UserRepresentation user = new UserRepresentation();
        user.setUsername(username);
        user.setEmail(email);
        user.setFirstName(name);
        user.setLastName(name);
        user.setEmailVerified(true);
        user.setEnabled(true);
        user.setCredentials(List.of(credential));

        jakarta.ws.rs.core.Response response = keycloak.realm(REALM).users().create(user);
        int status = response.getStatus();
        System.out.println("=== Keycloak create status: " + status);

        if (status != 201) {
            throw new RuntimeException("Failed to create user in Keycloak: " + status);
        }

        String userId = keycloak.realm(REALM).users()
                .searchByEmail(email, true).get(0).getId();

        RoleRepresentation realmRole = keycloak.realm(REALM)
                .roles().get(role).toRepresentation();

        keycloak.realm(REALM).users().get(userId)
                .roles().realmLevel().add(List.of(realmRole));

        System.out.println("=== Keycloak user created successfully: " + username);
    }

    // ═══════════════════════════════════════════════════════════
    // USER (Admin)
    // ═══════════════════════════════════════════════════════════

    public List<User> getAllUsers() { 
        return userRepository.findAll(); 
    }

    public Optional<User> getUserById(Long id) { 
        return userRepository.findById(id); 
    }

    public Optional<User> getUserByEmail(String email) { 
        return userRepository.findByEmail(email); 
    }

    public User createAdmin(User user, String password) {
        User saved = userRepository.save(user);
        createKeycloakUser(user.getName(), user.getEmail(), password, "ADMIN");
        return saved;
    }

    public void deleteUser(Long id) { 
        userRepository.deleteById(id); 
    }

    // ═══════════════════════════════════════════════════════════
    // STUDENT
    // ═══════════════════════════════════════════════════════════

    public List<Student> getAllStudents() { 
        return studentRepository.findAll(); 
    }

    public Optional<Student> getStudentById(Long id) { 
        return studentRepository.findById(id); 
    }

    public Student createStudent(Student student, String password) {
        Student saved = studentRepository.save(student);
        createKeycloakUser(student.getName(), student.getEmail(), password, "STUDENT");
        return saved;
    }

    public Student updateStudent(Long id, Student updatedStudent) {
        return studentRepository.findById(id).map(student -> {
            student.setName(updatedStudent.getName());
            student.setEmail(updatedStudent.getEmail());
            student.setRole(updatedStudent.getRole());
            student.setSpecialite(updatedStudent.getSpecialite());
            student.setFiliere(updatedStudent.getFiliere());
            return studentRepository.save(student);
        }).orElseThrow(() -> new RuntimeException("Student not found with id: " + id));
    }

    // ═══════════════════════════════════════════════════════════
    // TEACHER
    // ═══════════════════════════════════════════════════════════

    public List<Teacher> getAllTeachers() { 
        return teacherRepository.findAll(); 
    }

    public Optional<Teacher> getTeacherById(Long id) { 
        return teacherRepository.findById(id); 
    }

    public Teacher createTeacher(Teacher teacher, String password) {
        Teacher saved = teacherRepository.save(teacher);
        createKeycloakUser(teacher.getName(), teacher.getEmail(), password, "TEACHER");
        return saved;
    }

    public Teacher updateTeacher(Long id, Teacher updatedTeacher) {
        return teacherRepository.findById(id).map(teacher -> {
            teacher.setName(updatedTeacher.getName());
            teacher.setEmail(updatedTeacher.getEmail());
            teacher.setRole(updatedTeacher.getRole());
            teacher.setSpecialite(updatedTeacher.getSpecialite());
            return teacherRepository.save(teacher);
        }).orElseThrow(() -> new RuntimeException("Teacher not found with id: " + id));
    }
}