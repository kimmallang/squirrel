package com.mallang.squirrel.domain.humor;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum HumorOriginSiteType {
	ETOLAND("이토랜드", "etoland"),
	HUMORUNIV("웃긴대학", "humoruniv"),
	DCINSIDE("디시인사이드", "dcinside"),
	PPOMPPU("뽐뿌", "ppomppu");

	private final String name;
	private final String code;

	public static HumorOriginSiteType findByCode(String code) {
		for (HumorOriginSiteType originSiteType: HumorOriginSiteType.values()) {
			if (originSiteType.getCode().equals(code)) {
				return originSiteType;
			}
		}

		return null;
	}
}
