/*
 *	This file is part of Buzu.
 *
 *   Buzu is free software: you can redistribute it and/or modify
 *   it under the terms of the GNU Lesser General Public License as published by
 *   the Free Software Foundation, either version 3 of the License, or
 *   (at your option) any later version.
 *
 *   Buzu is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU Lesser General Public License for more details.
 *
 *   You should have received a copy of the GNU Lesser General Public License
 *   along with Buzu.  If not, see <http://www.gnu.org/licenses/>.
 */
package br.net.buzu.context;

import br.net.buzu.metadata.*;
import br.net.buzu.pplspec.api.MetadataFactory;
import br.net.buzu.pplspec.model.MetaInfo;
import br.net.buzu.pplspec.model.Metadata;
import br.net.buzu.util.StaticBehave;

import java.util.List;

/**
 * Simple Metadata Factory
 *
 * @author Douglas Siviotti
 * @since 1.0
 *
 */
public class BasicMetadataFactory implements MetadataFactory {

	public static final BasicMetadataFactory INSTANCE = new BasicMetadataFactory();

	@Override
	public Metadata create(MetaInfo metaInfo, List<? extends Metadata> children) {
		return children != null && !children.isEmpty() 
				? createComplex(metaInfo, (List<Metadata>) children)
				: createSimple(metaInfo );
	}

	protected BasicMetadata createComplex(MetaInfo metaInfo, List<Metadata> children) {
		return StaticBehave.isStaticChidren(children)
				? new ComplexStaticMetadada(metaInfo, children) 
				: new ComplexMetadata(metaInfo, children);
	}

	protected BasicMetadata createSimple(MetaInfo metaInfo) {
		return metaInfo.isStatic() 
				? new SimpleStaticMetadata(metaInfo)
				: new SimpleMetadata(metaInfo);
	}

}
