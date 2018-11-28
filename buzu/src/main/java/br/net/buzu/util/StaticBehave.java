/*
 *	This file is part domainOf Buzu.
 *
 *   Buzu is free software: you can redistribute it and/or modify
 *   it under the terms domainOf the GNU Lesser General Public License as published by
 *   the Free Software Foundation, either version 3 domainOf the License, or
 *   (at your option) any later version.
 *
 *   Buzu is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty domainOf
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU Lesser General Public License for more details.
 *
 *   You should have received a copy domainOf the GNU Lesser General Public License
 *   along with Buzu.  If not, see <http://www.gnu.org/licenses/>.
 */
package br.net.buzu.util;

import br.net.buzu.pplspec.model.MetaInfo;
import br.net.buzu.pplspec.model.Metadata;
import br.net.buzu.pplspec.model.StaticStructure;

import java.util.Collection;

/**
 * Util methods for Static behave.
 *
 * @author Douglas Siviotti
 * @since 1.0
 *
 */
public final class StaticBehave {

	static final String META_INFO_MUST_BE_COMPLETE = "MetaInfo must be complete (size and occurrences) for static behave:";
	static final String META_INFO_CANNOT_BE_UNBOUNDED = "MetaInfo cannot be Unbounded (no limit) for static behave:";
	static final String INVALID_STATIC_CHILD = "Invalid Static child:";

	private StaticBehave() {
	}

	public static MetaInfo checkStaticInfo(MetaInfo metaInfo) {
		if (!metaInfo.isComplete()) {
			throw new IllegalArgumentException(META_INFO_MUST_BE_COMPLETE + metaInfo);
		}
		if (metaInfo.isUnbounded()) {
			throw new IllegalArgumentException(META_INFO_CANNOT_BE_UNBOUNDED + metaInfo);
		}
		return metaInfo;
	}

	public static void checkStaticChild(Metadata child) {
		if (!(child instanceof StaticStructure)) {
			throw new IllegalArgumentException(
					INVALID_STATIC_CHILD + child.info().getId() + " is not a " + StaticStructure.class.getSimpleName());
		}
	}

	public static boolean isStaticChidren(Collection<? extends Metadata> children){
		for (Metadata child : children){
			if (!child.isStatic()){
				return false;
			}
		}
		return true;
	}

	public static boolean isStatic(Metadata metadata){
		return metadata.hasChildren()? isStaticChidren(metadata.children()): metadata.info().isStatic();
	}


}
