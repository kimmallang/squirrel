package com.mallang.squirrel.infrastructure;

import java.util.List;

import javax.sql.DataSource;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.util.CollectionUtils;

import com.mallang.squirrel.domain.Hello;

@MybatisTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class HelloMapperTest {
	protected final JdbcTemplate jdbcTemplate = new JdbcTemplate();

	@Autowired
	private DataSource dataSource;

	@Autowired
	private HelloMapper helloMapper;

	@BeforeEach
	public void beforeEach() {
		jdbcTemplate.setDataSource(dataSource);
	}

	private void insertDummy(int num) {
		final String title = "제목" + num;
		final String contents = "내용" + num;
		final String query = "INSERT INTO hello"
			+ " (title, contents, created_at, modified_at)"
			+ " VALUE"
			+ " (?, ?, NOW(), NOW());";
		jdbcTemplate.update(query, title, contents);
	}

	@Test
	public void HELLO_테이블_전체_목록_조회() {
		// given
		insertDummy(1);
		insertDummy(2);

		// when
		final List<Hello> helloList = helloMapper.findAll();

		// then
		Assertions.assertFalse(CollectionUtils.isEmpty(helloList));
		Assertions.assertTrue(helloList.size() > 0);
	}

	@Test
	public void HELLO_테이블_단건_조회() {
		// given
		insertDummy(1);
		final String query = "SELECT id FROM hello ORDER BY ID DESC LIMIT 1";
		final Long latestId = jdbcTemplate.queryForObject(query, Long.class);

		// when
		final Hello hello = helloMapper.findById(latestId);

		// then
		Assertions.assertEquals("제목1", hello.getTitle());
		Assertions.assertEquals("내용1", hello.getContents());
	}

	@Test
	public void HELLO_테이블_INSERT() {
		// given
		final Hello hello = Hello.builder()
			.title("제목1")
			.contents("내용1")
			.build();

		// when
		helloMapper.save(hello);

		// then
		final String query = "SELECT * FROM hello ORDER BY ID DESC LIMIT 1";
		final Hello helloResult = jdbcTemplate.queryForObject(query, (rs, rowNum) ->
			Hello.builder()
				.id(rs.getLong("id"))
				.title(rs.getString("title"))
				.contents(rs.getString("contents"))
				.build());
		Assertions.assertNotNull(helloResult);
		Assertions.assertEquals("제목1", helloResult.getTitle());
		Assertions.assertEquals("내용1", helloResult.getContents());
	}

	@Test
	public void HELLO_테이블_DELETE() {
		// given
		insertDummy(1);

		final String findIdQuery = "SELECT id FROM hello ORDER BY ID DESC LIMIT 1";
		final Long latestId = jdbcTemplate.queryForObject(findIdQuery, Long.class);

		final String countAllQuery = "SELECT COUNT(1) FROM hello";
		final Integer countAllBefore = jdbcTemplate.queryForObject(countAllQuery, Integer.class);

		// when
		helloMapper.delete(latestId);

		// then
		final Integer countAllAfter = jdbcTemplate.queryForObject(countAllQuery, Integer.class);
		Assertions.assertNotEquals(countAllAfter, countAllBefore);
	}
}
