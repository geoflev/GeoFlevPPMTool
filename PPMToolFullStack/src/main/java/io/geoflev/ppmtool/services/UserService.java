package io.geoflev.ppmtool.services;

import io.geoflev.ppmtool.domain.User;
import io.geoflev.ppmtool.exceptions.UsernameAlreadyExistsException;
import io.geoflev.ppmtool.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    public User saveUser(User newUser){
        try {
            newUser.setPassword(bCryptPasswordEncoder.encode(newUser.getPassword()));
            newUser.setUsername(newUser.getUsername());
            //cant JsonIgnore it so we set it to ""
            newUser.setConfirmPassword("");
            return  userRepository.save(newUser);
        }catch (Exception e){
            throw new UsernameAlreadyExistsException("Username '" + newUser.getUsername() + "' already exists");
        }

    }
}
