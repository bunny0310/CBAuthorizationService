package com.cb.authorization.services;

import com.cb.authorization.database.DAO.TokenDAO;
import com.cb.authorization.database.models.Token;

import java.util.List;

public class Auth {

    private TokenDAO tokenDAO;
    public Auth(TokenDAO tokenDAO) {
        this.tokenDAO = tokenDAO;
    }

    public String saveAuth(String email, String hash) {
        int result = this.tokenDAO.saveHash(email, hash);
        return result ==1 ? hash : "error";
    }

    public List<Token> verifyToken(String token) {
        return tokenDAO.getTokenObject(token);
    }
    public List<Token> getToken(String email) {
        return tokenDAO.getToken(email);
    }

}
