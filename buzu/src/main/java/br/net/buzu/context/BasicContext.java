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
package br.net.buzu.context;

import br.net.buzu.pplspec.context.*;
import br.net.buzu.pplspec.lang.Syntax;

import java.util.Objects;

/**
 * Context domainOf parsing/serialization with Factories used to create objects and
 * others extension points.
 * 
 *
 * @author Douglas Siviotti
 * @since 1.0
 *
 */
public class BasicContext implements PplContext {

	private final Syntax syntax;
	private final SubtypeManager subtypeManager;
	private final MetadataFactory metadataFactory;
	private final CoderManager coderManager;
	private final ParserFactory parserFactory;

	public BasicContext() {
		this(new Syntax(), BasicSubtypeManager.INSTANCE, BasicMetadataFactory.INSTANCE, BasicCoderManager.INSTANCE,
				BasicParserFactory.INSTANCE);
	}

	public BasicContext(Syntax syntax, SubtypeManager subtypeManager, MetadataFactory metadataFactory,
			CoderManager coderManager, ParserFactory parserFactory) {
		super();
		this.syntax = Objects.requireNonNull(syntax, "syntax cannot be null");
		this.subtypeManager = Objects.requireNonNull(subtypeManager, "subtypeManager cannot be null");
		this.metadataFactory = Objects.requireNonNull(metadataFactory, "metadataFactory cannot be null");
		this.coderManager = Objects.requireNonNull(coderManager, "coderManager cannot be null");
		this.parserFactory = Objects.requireNonNull(parserFactory, "parserFactory cannot be null");
	}

	// **************************************************
	// API
	// **************************************************

	@Override
	public SubtypeManager subtypeManager() {
		return subtypeManager;
	}

	@Override
	public MetadataFactory metadataFactory() {
		return metadataFactory;
	}

	@Override
	public Syntax syntax() {
		return syntax;
	}

	@Override
	public CoderManager coderManager() {
		return coderManager;
	}

	@Override
	public ParserFactory parserFactory() {
		return parserFactory;
	}

}
