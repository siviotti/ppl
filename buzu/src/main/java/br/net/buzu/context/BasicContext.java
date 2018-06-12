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

import java.util.Objects;

import br.net.buzu.pplspec.context.CoderManager;
import br.net.buzu.pplspec.context.MetadataFactory;
import br.net.buzu.pplspec.context.ParserFactory;
import br.net.buzu.pplspec.context.PplContext;
import br.net.buzu.pplspec.context.SubtypeManager;
import br.net.buzu.pplspec.lang.Syntax;

/**
 * Context of parsing/serialization with Factories used to create objects and
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
		this(Syntax.INSTANCE, BasicSubtypeManager.INSTANCE, BasicMetadataFactory.INSTANCE, BasicCoderManager.INSTANCE,
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
