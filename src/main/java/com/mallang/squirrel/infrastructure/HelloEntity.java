package com.mallang.squirrel.infrastructure;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@Builder
@Table(name = "hello")
@NoArgsConstructor
@AllArgsConstructor
public class HelloEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long id;

	@Column(name = "title")
	private String title;

	@Column(name = "contents")
	private String contents;

	@Column(name = "created_at", updatable = false)
	protected LocalDateTime createdAt;

	@Column(name = "modified_at")
	protected LocalDateTime modifiedAt;

	@PrePersist
	void created() {
		this.createdAt = LocalDateTime.now();
	}

	@PreUpdate
	void updated() {
		this.modifiedAt = LocalDateTime.now();
	}
}
