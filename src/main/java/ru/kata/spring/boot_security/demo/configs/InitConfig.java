package ru.kata.spring.boot_security.demo.configs;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.kata.spring.boot_security.demo.models.Role;
import ru.kata.spring.boot_security.demo.models.User;
import ru.kata.spring.boot_security.demo.services.RoleServiceImpl;
import ru.kata.spring.boot_security.demo.services.UserServiceImpl;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
public class InitConfig {
    private final UserServiceImpl userService;
    private final RoleServiceImpl roleService;

    @Autowired
    public InitConfig(UserServiceImpl userService, RoleServiceImpl roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }

    public void init() {
        List<Role> roleList = Role.getRolesListDefault();
        final Set<Role> roles1 = new HashSet<>(roleList); //admin user
        final Set<Role> roles2 = new HashSet<>(List.of(roleList.get(1))); //user
        List<User> userList = User.getUsersListDefault();
        userList.get(0).setRoles(roles1);
        userList.get(1).setRoles(roles2);
        //Подготовка базы данных
        for (Role role: roleList) {
            roleService.saveRole(role);
        }
        for (User user: userList) {
            userService.saveUser(user, null);
        }
    }
}
