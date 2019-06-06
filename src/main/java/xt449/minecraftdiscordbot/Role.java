package xt449.minecraftdiscordbot;

import me.lucko.luckperms.api.Group;

/**
 * @author xt449
 * Copyright BinaryBanana/xt449 2019
 * All Rights Reserved
 */
class Role {

	final Group group;
	final String name;
	/*final String prefix;*/
	final long id;

	Role(String name, /*String prefix,*/ long id, Group group) {
		this.name = name;
		/*this.prefix = prefix;*/
		this.id = id;
		this.group = group;
	}
}
