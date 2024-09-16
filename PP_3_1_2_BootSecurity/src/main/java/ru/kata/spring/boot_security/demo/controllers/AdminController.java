package ru.kata.spring.boot_security.demo.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import ru.kata.spring.boot_security.demo.entities.Role;
import ru.kata.spring.boot_security.demo.entities.User;
import ru.kata.spring.boot_security.demo.repositories.RoleRepository;
import ru.kata.spring.boot_security.demo.services.RegistrationService;
import ru.kata.spring.boot_security.demo.services.UserService;

import javax.validation.Valid;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


@Controller()
@RequestMapping("/admin")
public class AdminController {

    private final UserService userService;
    private final RoleRepository roleRepository;
    private final RegistrationService registrationService;


    @Autowired
    public AdminController(UserService userService, RoleRepository roleRepository, RegistrationService registrationService) {
        this.userService = userService;
        this.roleRepository = roleRepository;
        this.registrationService = registrationService;
    }

    @GetMapping()
    public String adminPage() {
        return "admin";
    }

    @GetMapping("/showAllUser")
    public String showAllUser(Model model) {
        model.addAttribute("allUsers", userService.showAllUser());
        return "allUsers";
    }

    @GetMapping(value = "/addNewUser")
    public ModelAndView addNewUser() {
        User user = new User();
        ModelAndView maw = new ModelAndView("userInfo");
        maw.addObject("user", user);
        List<Role> listRoles = roleRepository.findAll();
        Set<Role> setRoles = new HashSet<>(listRoles);
        maw.addObject("allRoles", setRoles);
        return maw;
    }

    @PostMapping("/saveUser")
    public String saveNewUser(@ModelAttribute("user") @Valid User user,
                              BindingResult bindingResult) {

        if (bindingResult.hasErrors())
            return "userInfo";

        registrationService.saveUser(user);
        return "redirect:/admin/showAllUser";
    }

    @GetMapping("/edit")
    public ModelAndView editUser(@RequestParam("id") Long id) {
        User user = userService.getUserById(id);
        ModelAndView mav = new ModelAndView("userInfo");
        mav.addObject("user", user);

        List<Role> listRoles = roleRepository.findAll();
        Set<Role> setRoles = new HashSet<>(listRoles);

        mav.addObject("allRoles", setRoles);

        return mav;
    }

    @PostMapping("/delete")
    public String delete(@RequestParam("id") int id) {
        userService.deleteUser(id);
        return "redirect:/admin/showAllUser";
    }

}