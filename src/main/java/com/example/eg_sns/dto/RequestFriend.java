package com.example.eg_sns.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * ログインDTOクラス。
 *
 * @author tomo-sato
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class RequestFriend extends DtoBase {

	private String status;
	
	private Long usersId;
	
	private Long friendId;
}
