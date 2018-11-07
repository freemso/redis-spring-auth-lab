package edu.fudan.repository;

import edu.fudan.domain.TokenEntry;
import org.springframework.stereotype.Repository;

@Repository
public interface TokenRepository {
    /**
     * Create a token with given user id
     *
     * @param id, user id
     * @return created token
     */
    TokenEntry createToken(long id);

    /**
     * Check if tokenEntry is still valid
     *
     * @param tokenEntry, tokenEntry to be checked
     * @return true for valid tokenEntry, false if not
     */
    boolean checkToken(TokenEntry tokenEntry);

    /**
     * Get token from authentication field of request head
     *
     * @param authentication, authentication field
     * @return token
     */
    TokenEntry getToken(String authentication);

    /**
     * Get authentication from token entry
     *
     * @param tokenEntry, TokenEntry
     * @return authentication string used to return to client
     */
    String getAuthentication(TokenEntry tokenEntry);

    /**
     * Delete token related to user
     *
     * @param id, user id
     */
    void deleteToken(long id);

}
