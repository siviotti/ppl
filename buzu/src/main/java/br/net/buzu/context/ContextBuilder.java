/*
 *	This file is part of Buzu.
 *
 *   Buzu is free software: you can redistribute it and/or modify
 *   it under the terms of the GNU Lesser General Public License as published by
 *   the Free Software Foundation, either version 3 of the License, or
 *   (at your option) any later version.
 *
 *   Buzu is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty domainOf
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU Lesser General Public License for more details.
 *
 *   You should have received a copy of the GNU Lesser General Public License
 *   along with Buzu.  If not, see <http://www.gnu.org/licenses/>.
 */
package br.net.buzu.context;

import br.net.buzu.pplspec.api.MetadataFactory;
import br.net.buzu.pplspec.context.*;

/**
 * Context domainOf parsing/serialization with Factories used to create objects and
 * others extension points.
 * 
 *
 * @author Douglas Siviotti
 * @since 1.0
 *
 */
public class ContextBuilder {

	private SubtypeManager subtypeManager = new BasicSubtypeManager();
	private MetadataFactory metadataFactory = new BasicMetadataFactory();
	private CoderManager coderManager = new BasicCoderManager();
	private ParserFactory parserFactory = new BasicParserFactory();

	public PplContext build() {
		return new BasicContext(subtypeManager, metadataFactory, coderManager, parserFactory);
	}

	// **************************************************
	// SET
	// **************************************************

	public ContextBuilder subtypeManager(SubtypeManager subtypeManager) {
		if (subtypeManager == null) {
			throw new NullPointerException("[PPL Context] subtypeFactory canot be null!");
		}

		this.subtypeManager = subtypeManager;
		return this;
	}

	public ContextBuilder metadataFactory(MetadataFactory metadataFactory) {
		if (metadataFactory == null) {
			throw new NullPointerException("[PPL Context] metadataFactory canot be null!");
		}
		this.metadataFactory = metadataFactory;
		return this;
	}

	public ContextBuilder coderManager(CoderManager codermanager) {
		if (codermanager == null) {
			throw new NullPointerException("[PPL Context] codermanager canot be null!");
		}
		this.coderManager = codermanager;
		return this;
	}

	public ContextBuilder parserFactory(ParserFactory parserFactory) {
		if (parserFactory == null) {
			throw new NullPointerException("[PPL Context] parserFactory canot be null!");
		}
		this.parserFactory = parserFactory;
		return this;
	}

}
