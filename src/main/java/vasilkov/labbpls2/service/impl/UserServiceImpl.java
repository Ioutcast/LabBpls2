package vasilkov.labbpls2.service.impl;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import nu.xom.ParsingException;
import org.springframework.stereotype.Service;
import vasilkov.labbpls2.entity.User;

import java.io.IOException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl {
    public Optional<User> getByEmail(@NonNull String login) throws ParsingException, IOException {
        return XMLServiceImpl.getByEmailFromXml(login);
    }

}