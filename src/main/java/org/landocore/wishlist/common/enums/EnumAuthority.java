package org.landocore.wishlist.common.enums;

/**
 * enumeration of authority types.
 * @author LANDSBERG-S
 *
 */
public enum EnumAuthority {
	
	ROLE_USER(1L),
	ROLE_ADMIN(2L);

	private long idAuthority;
	
	EnumAuthority(Long idAuthority) {
		this.idAuthority = idAuthority;
	}
	
	public Long getIdAuthority() {
		return this.idAuthority;
	}

}
