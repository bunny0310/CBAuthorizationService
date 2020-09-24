package com.cb.authorization.database.DAO;

import com.cb.authorization.database.models.User;
import com.cb.authorization.database.users.UsersMapper;
import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.BindBean;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.SqlUpdate;
import org.skife.jdbi.v2. sqlobject.customizers.RegisterMapper;

import java.util.List;

@RegisterMapper(UsersMapper.class)
public interface UsersDAO {

    @SqlQuery("SELECT * FROM users WHERE id = :id")
    public User getUser(@Bind("id") final int id);

    @SqlQuery("SELECT * FROM users")
    public List<User> getUsers();

}
