package com.server.server.controller;

import com.server.server.converter.UserConverter;
import com.server.server.dto.UsersDto;
import com.server.server.service.UsersService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class UserController {

    private final UsersService userService;

    private final UserConverter userConverter;


    @GetMapping("/findAllUsers")
    public List<UsersDto> findAllUsers() {
        var users = userService.getAllUsers();
        return userConverter.convertModelListToDtoList(users);
    }
}
