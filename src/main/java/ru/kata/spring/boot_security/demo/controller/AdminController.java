package ru.kata.spring.boot_security.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.repositories.RoleRepository;
import ru.kata.spring.boot_security.demo.services.RoleService;
import ru.kata.spring.boot_security.demo.services.UserService;

import javax.validation.Valid;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Controller
@RequestMapping(value = "/admin")
public class AdminController {

    private final UserService userService;
    private final RoleService roleService;

    @Autowired
    public AdminController(UserService userService, RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.setDisallowedFields("roles");
    }
    @GetMapping
    public String allUsers(Model model) {
        model.addAttribute("findAll", userService.findAll());
        return "admin";
    }

    @GetMapping(value = "/new")
    public String createUserForm(Model model) {
        model.addAttribute("user", new User());
        model.addAttribute("roleList", roleService.findAllRoles());
        return "create-user";
    }

    @PostMapping(value = "/new")
    public String createUser(@Valid @ModelAttribute("user") User user,
                             @RequestParam(value = "roles",required = false) List<String> roles,
                             BindingResult bindingResult) {
        if(bindingResult.hasErrors()) {
            return "create-user";
        }
        Set<Role> userRoles = roles.stream()
                .map(roleService::findByRoleName)
                .collect(Collectors.toSet());
        user.setRoles(userRoles);
        userService.save(user);
        return "redirect:/admin";
    }

    @GetMapping(value = "/edit")
    public String editUserForm(@RequestParam(value = "id") int id, Model model) {
        model.addAttribute("user", userService.findOne(id));
        model.addAttribute("roleList", roleService.findAllRoles());
        return "edit-user";
    }

    @PostMapping(value = "/edit")
    public String editUser(@Valid@ModelAttribute("user") User user,
                           @RequestParam(value = "roles",required = false) List<String> roles,BindingResult
                           bindingResult) {
        if(bindingResult.hasErrors()) {
            return "edit-user";
        }
        Set<Role> userRoles = roles.stream()
                .map(roleService::findByRoleName)
                .collect(Collectors.toSet());
        user.setRoles(userRoles);
        userService.update(user.getId(), user);
        return "redirect:/admin";
    }

    @PostMapping(value = "/delete")
    public String deleteUser(@RequestParam(value = "id") int id) {
        userService.delete(id);
        return "redirect:/admin";
    }
}
