package edu.fudan.model;

import edu.fudan.domain.TokenEntry;
import edu.fudan.domain.User;
import edu.fudan.repository.TokenRepository;
import edu.fudan.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static edu.fudan.config.Constants.ID_LENGTH;

@Service
@Transactional
public class UserService {

    private final UserRepository userRepository;

    private final TokenRepository tokenRepository;

    @Autowired
    public UserService(UserRepository userRepository, TokenRepository tokenRepository) {
        this.userRepository = userRepository;
        this.tokenRepository = tokenRepository;
    }

    /**
     * Given email and password, create a token entry and return it as a authentication response
     *
     * @param email,    email of the account
     * @param password, password of the account
     * @return a string of authentication
     */
    public String createToken(String email, String password) {
        // Lower case
        email = email.toLowerCase();

        User user = this.userRepository.findByEmail(email).orElseThrow(
                () -> new RuntimeException("Email does not exist") // Email does not exist
        );

        if (!user.getPassword().equals(password)) {  // Wrong password
            throw new RuntimeException("Wrong password");
        }

        // Generate a token
        TokenEntry tokenEntry = tokenRepository.createToken(user.getUserId());

        // Generate authentication from this token

        return tokenRepository.getAuthentication(tokenEntry);
    }

    /**
     * Given the user id, delete the token from repository
     *
     * @param userId, id of the current user
     */
    public void deleteToken(long userId) {
        tokenRepository.deleteToken(userId);
    }

    /**
     * Get user private data by id
     *
     * @return the private data of the user
     */
    public User getUserPrivate(User currentUser, long userId) {
        if (currentUser.getUserId() != userId) {
            throw new RuntimeException("Not this user.");
        }

        return this.userRepository.findById(userId).orElseThrow(
                () -> new RuntimeException("User not found") // User not found
        );
    }

    /**
     * Create a new user
     *
     * @return user private data
     */
    public User createUser(String email, String name, String password) {
        // Lower case
        email = email.toLowerCase();

        // Check if the email exists
        if (this.userRepository.findByEmail(email).isPresent()) {
            throw new RuntimeException("Email: " + email + " does not exist.");
        }

        // Create a new id for user
        long newUserId = this.generateRandomId();

        // Create a new user based on the user type
        User newUser = new User(newUserId, name, password, email);

        // Add this newly created user to user repository
        return this.userRepository.save(newUser);
    }


    /**
     * Generate a unique user id
     *
     * @return a user id
     */
    private long generateRandomId() {
        while (true) {
            long randomLong = this.generateRandomLongId();
            // Check if the id exists as a user id
            if (!this.userRepository.existsById(randomLong)) {
                return randomLong;
            }
        }
    }

    private long generateRandomLongId() {
        long lower = (long) Math.pow(10, ID_LENGTH);
        long upper = (long) Math.pow(10, ID_LENGTH + 1);
        return lower + (long) (Math.random() * (upper - lower));
    }

}
