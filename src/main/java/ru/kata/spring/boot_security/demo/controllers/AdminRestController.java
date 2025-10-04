package ru.kata.spring.boot_security.demo.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.models.Role;
import ru.kata.spring.boot_security.demo.models.User;
import ru.kata.spring.boot_security.demo.services.RoleServiceImpl;
import ru.kata.spring.boot_security.demo.services.UserServiceImpl;

import java.util.List;

@RestController
@RequestMapping("admin/api")
public class AdminRestController {
    private final UserServiceImpl userService;

    private final RoleServiceImpl roleService;

    @Autowired
    public AdminRestController(UserServiceImpl userService, RoleServiceImpl roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }

    @GetMapping("/roles")
    public ResponseEntity<List<Role>> getAllRoles() {
        List<Role> roleList = roleService.findAllRoles();
        return new ResponseEntity<>(roleList, HttpStatus.OK);
    }

    @GetMapping("/users")
    public ResponseEntity<List<User>> showAllUsers() {
        List<User> userList = userService.findAllUsers();
        return new ResponseEntity<>(userList, HttpStatus.OK);
    }

    @GetMapping("/users/{id}")
    public ResponseEntity<User> getUser(@PathVariable(name = "id") Long id) {
        User user = userService.findByIdUsers(id);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @PostMapping("/users")
    public ResponseEntity<User> addNewUser(@RequestBody User user) {
        userService.saveUser(user, null);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @GetMapping("/auth")
    public ResponseEntity<User> showAuthUser(Authentication auth) {
        User user = userService.findByUsername(auth.getName());
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @PatchMapping("/users/{id}")
    public ResponseEntity<User> update(@RequestBody User user, @PathVariable Long id) {
        userService.updateUser(id, user, null);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @DeleteMapping("/users/{id}")
    public ResponseEntity<Long> delete(@PathVariable("id") Long id) {
        userService.deleteByIdUsers(id);
        return new ResponseEntity<>(id, HttpStatus.OK);
    }
}
