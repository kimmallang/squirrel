package com.mallang.squirrel.infrastructure;

import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.util.CollectionUtils;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class HelloRepositoryTest {
	@Autowired
	private HelloRepository helloRepository;

	@Test
	public void HELLO_테이블_전체_목록_조회() {
		// given
		HelloEntity hello = HelloEntity.builder()
			.title("제목99")
			.contents("내용99")
			.build();

		// when
		helloRepository.save(hello);
		final List<HelloEntity> helloList = helloRepository.findAll();

		// then
		Assertions.assertFalse(CollectionUtils.isEmpty(helloList));
		Assertions.assertTrue(helloList.size() > 0);
	}
}
