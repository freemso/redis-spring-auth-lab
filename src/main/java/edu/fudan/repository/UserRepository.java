package edu.fudan.repository;


import edu.fudan.domain.User;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends Neo4jRepository<User, Long>{
    Optional<User> findByEmail(String email);

    List<User> findByName(String name);


    boolean existsByEmail(String email);

    @Override
    void delete(User user);
}
