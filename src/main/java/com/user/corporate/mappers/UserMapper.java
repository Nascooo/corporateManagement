package com.user.corporate.mappers;


import com.user.corporate.entities.User;
import com.user.corporate.models.UserModel;
import com.user.corporate.responses.UserCreationResponse;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface UserMapper {

    User fromUserModelToUser(UserModel userModel);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "userId", ignore = true)
    void mergeUserAndIgnoreNull(User source, @MappingTarget User target);

    UserCreationResponse fromUserToUserResponse(User user);
}
