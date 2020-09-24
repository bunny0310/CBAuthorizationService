package com.cb.authorization.database.tokens;

import com.cb.authorization.database.models.Token;
import com.cb.authorization.database.models.User;
import org.skife.jdbi.v2.StatementContext;
import org.skife.jdbi.v2.tweak.ResultSetMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class TokensMapper implements ResultSetMapper<Token> {
    @Override
    public Token map(int index, ResultSet r, StatementContext ctx) throws SQLException {

        Token user = new Token(
                r.getString("email"),
                r.getString("token")
        );

        return user;
    }
}
