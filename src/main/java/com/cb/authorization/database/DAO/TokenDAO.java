package com.cb.authorization.database.DAO;

import com.cb.authorization.database.models.Token;
import com.cb.authorization.database.tokens.TokensMapper;
import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.SqlUpdate;
import org.skife.jdbi.v2.sqlobject.customizers.RegisterMapper;

import java.util.List;

@RegisterMapper(TokensMapper.class)
public interface TokenDAO {

    @SqlUpdate("INSERT INTO tokens (email, token) VALUES(:email, :hash)")
    public int saveHash(@Bind("email") final String email, @Bind("hash") final String hash);

    @SqlQuery("SELECT * FROM tokens WHERE token = :token")
    public List<Token> getTokenObject(@Bind("token") final String token);

    @SqlQuery("SELECT * FROM tokens WHERE email = :email")
    public List<Token> getToken(@Bind("email") final String email);

    @SqlUpdate("DELETE FROM tokens WHERE TIMESTAMPDIFF(HOUR, createdAt, current_timestamp) >= 4")
    public void deleteExpiredTokens();
}
