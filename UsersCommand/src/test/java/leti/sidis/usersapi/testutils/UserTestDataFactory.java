package leti.sidis.usersapi.testutils;


import leti.sidis.users.api.CreateUserRequest;
import leti.sidis.users.api.UserView;
import leti.sidis.users.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

import java.util.UUID;

import static org.springframework.test.util.AssertionErrors.assertEquals;
import static org.springframework.test.util.AssertionErrors.assertNotNull;

@Service
public class UserTestDataFactory {
/*

    @Autowired
    private UserService userService;

    @Transactional
    public UserView createUser(final String username, final String fullName, final String password) {
        final CreateUserRequest createRequest = new CreateUserRequest(username, fullName, password);

        final UserView userView = userService.create(createRequest);

        assertNotNull(userView.getId(), "User id must not be null!");
        assertEquals(fullName, userView.getFullName(), "User name update isn't applied!");

        return userView;
    }

    public UserView createUser(final String username, final String fullName) {
        return createUser(username, fullName, "Test12345_");
    }

    @Transactional
    public void deleteUser(final UUID id) {
        userService.delete(id);
    }
*/

}
