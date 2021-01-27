package ru.holuhoev.social_network.adapter.repository;

import lombok.AllArgsConstructor;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.holuhoev.social_network.core.domain.entity.Friend;
import ru.holuhoev.social_network.core.domain.port.FriendRepository;

import javax.annotation.Nonnull;
import java.util.UUID;

@Repository
@AllArgsConstructor
public class FriendRepositoryImpl implements FriendRepository {
    private final NamedParameterJdbcTemplate jdbcTemplate;

    @Override
    public boolean existsByUserIds(
            @Nonnull final UUID firstUserId, @Nonnull final UUID secondUserId
    ) {
        return jdbcTemplate.query(
                "SELECT * FROM friends " +
                "WHERE (from_user_id = :first_user_id AND to_user_id = :second_user_id) " +
                "OR (from_user_id = :second_user_id AND to_user_id = first_user_id) ",
                new MapSqlParameterSource()
                        .addValue("first_user_id", firstUserId)
                        .addValue("second_user_id", secondUserId),
                (rs, i) -> rs.getLong("count") > 0
        ).get(0);
    }


    @Override
    public void create(@Nonnull final Friend friend) {
        final String fromUserIdStr = friend.getFromUserId().toString();
        final String toUserIdStr = friend.getToUserId().toString();
        jdbcTemplate.update(
                "INSERT INTO friends (from_user_id, to_user_id) VALUES (:from_user_id, :to_user_id)",
                new MapSqlParameterSource()
                .addValue("from_user_id", fromUserIdStr)
                .addValue("to_user_id", toUserIdStr)
//                        .addValue("from_user_id", "0000b6f5-0995-4e0b-964e-e4f47f909200")
//                        .addValue("to_user_id", "00978b20-0c04-42b9-b335-51e20611c118")
        );
    }
}
