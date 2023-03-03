package com.chuwa.redbook.dao;

import com.chuwa.redbook.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author b1go
 * @date 8/22/22 6:48 PM
 */
@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
    // No need to write code

    /**
     * 找到该时间段内所有段帖子
     * @param startTime
     * @param endTime
     * @return
     */
    @Query(value = "select * from posts where create_date_time between :startTime and :endTime", nativeQuery = true)
    List<Post> getAllPostIdsByCreateDateBetweenStartTimeAndEndTime(LocalDateTime startTime, LocalDateTime endTime);
}
