package com.server.server.converter;

import com.server.server.dto.UsersDto;
import com.server.server.model.Users;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class UserConverter implements IConverter<Users, UsersDto> {
    @Override
    public Users convertDtoToModel(UsersDto usersDto) {
        return Users.builder()
                .id(usersDto.getId())
                .name(usersDto.getName())
                .build();
    }

    @Override
    public UsersDto convertModelToDto(Users users) {
        if (users == null)
            return new UsersDto();
        return UsersDto.builder()
                .id(users.getId())
                .name(users.getName())
                .build();
    }

    @Override
    public List<UsersDto> convertModelListToDtoList(List<Users> users) {
        return users
                .stream()
                .map(this::convertModelToDto)
                .collect(Collectors.toList());
    }
}
