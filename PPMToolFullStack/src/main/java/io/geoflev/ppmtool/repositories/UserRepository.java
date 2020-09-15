package io.geoflev.ppmtool.repositories;

import io.geoflev.ppmtool.domain.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends CrudRepository<User, Long> {

    User findByUsername(String username);

    User getById(Long id);

    //we wont use it
    //Optional prevents NullPointerException
    //Optional<User> findById(Long id);
}
