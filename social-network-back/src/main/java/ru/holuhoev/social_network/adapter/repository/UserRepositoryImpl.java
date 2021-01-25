package ru.holuhoev.social_network.adapter.repository;

import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import ru.holuhoev.social_network.core.domain.entity.User;
import ru.holuhoev.social_network.core.domain.port.UserRepository;

import javax.annotation.Nonnull;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class UserRepositoryImpl implements UserRepository {

    private final NamedParameterJdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert inserter;

    public UserRepositoryImpl(final NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.inserter = new SimpleJdbcInsert(jdbcTemplate.getJdbcTemplate())
                .withTableName("users");
    }


    @Nonnull
    @Override
    public Optional<User> loadOptByUsername(@Nonnull final String username) {
        return jdbcTemplate.query(
                "SELECT * FROM users WHERE username = :username",
                new MapSqlParameterSource()
                        .addValue("username", username),
                this::mapRow
        ).stream().findFirst();
    }


    @Override
    public void create(@Nonnull final User user) {
        jdbcTemplate.update(
                "INSERT INTO users (user_id, username, password, first_name, last_name, interests, city, age, create_ts) " +
                "VALUES (:user_id, :username, :password, :first_name, :last_name, :interests,  :city, :age, :create_ts)",
                doMapping(user)
        );
    }


    @Nonnull
    @Override
    public List<User> loadFriendUsers(@Nonnull final UUID userId) {
        return jdbcTemplate.query(
                "SELECT * " +
                "FROM users " +
                "         JOIN friends f ON users.user_id = f.from_user_id " +
                "WHERE to_user_id = user_id " +
                "UNION " +
                "SELECT * " +
                "FROM users " +
                "         JOIN friends f2 ON users.user_id = f2.to_user_id " +
                "WHERE from_user_id = user_id;",
                new MapSqlParameterSource().addValue("user_id", userId.toString()),
                this::mapRow
        );
    }


    @Nonnull
    @Override
    public List<User> loadUsers() {
        return jdbcTemplate.query(
                "SELECT * FROM users LIMIT 30",
                this::mapRow
        );
    }


    @Nonnull
    @Override
    public User loadById(@Nonnull final UUID userId) {
        return jdbcTemplate.query(
                "SELECT * FROM users WHERE user_id = :user_id",
                new MapSqlParameterSource().addValue("user_id", userId.toString()),
                this::mapRow
        ).get(0);
    }

    @Override
    public void insertBatch(@Nonnull final List<User> users) {
        inserter.executeBatch(
                users.stream()
                     .map(this::doMapping)
                     .toArray(MapSqlParameterSource[]::new)
        );
    }


    private MapSqlParameterSource doMapping(final User user) {
        return new MapSqlParameterSource()
                .addValue("user_id", user.getUserId().toString())
                .addValue("username", user.getUsername())
                .addValue("first_name", user.getFirstName())
                .addValue("last_name", user.getLastName())
                .addValue("password", user.getPassword())
                .addValue("age", user.getAge())
                .addValue("interests", user.getInterests())
                .addValue("city", user.getCity())
                .addValue("create_ts", user.getCreateTs());
    }


    private User mapRow(final ResultSet rs, final int column) throws SQLException {
        return new User(
                UUID.fromString(rs.getString("user_id")),
                rs.getString("username"),
                rs.getString("password"),
                rs.getString("first_name"),
                rs.getString("last_name"),
                rs.getInt("age"),
                rs.getString("interests"),
                rs.getString("city"),
                rs.getTimestamp("create_ts").toLocalDateTime()
        );
    }
}