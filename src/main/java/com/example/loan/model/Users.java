package com.example.loan.model;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.sql.Date;
import java.sql.Timestamp;

/**
 * The type User detail.
 */
@Entity
@Table(name="users")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Users {

	@Id
	@GeneratedValue(generator="SEQ_users_user_id")
	@SequenceGenerator(name="SEQ_users_user_id",sequenceName="SEQ_users_user_id", allocationSize=1)
	@Column(name="user_id")
	private long userId;

	private String name;

	private String pan;
}
