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
package br.net.buzu.metadata.build;

import br.net.buzu.context.BasicContext;
import br.net.buzu.metaclass.BasicMetaclassReader;
import br.net.buzu.metadata.build.load.BasicMetadataLoader;
import br.net.buzu.metadata.build.parse.BasicMetadataParser;
import br.net.buzu.api.MetaclassReader;
import br.net.buzu.api.MetadataLoader;
import br.net.buzu.ext.MetadataParser;
import br.net.buzu.context.JavaContext;
import br.net.buzu.exception.PplException;
import br.net.buzu.model.MetaInfo;
import br.net.buzu.model.Metadata;
import br.net.buzu.model.PplString;
import br.net.buzu.model.StaticMetadata;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

/**
 * Metadata Builder. Builds a Metadata from an Object source.
 * 
 * @author Douglas Siviotti
 * @since 1.0
 *
 */
public class MetadataBuilder {

	private static final String METADATA_IS_NOT_STATIC = "The metadata is not Static:\n";
	private final JavaContext context;
	private final MetadataLoader loader;
	private final MetadataParser parser;
	private final MetaclassReader reader;

	/**
	 * Complete Constructor.
	 * 
	 * @param context
	 * @param loader
	 * @param parser
	 * @param reader
	 */
	public MetadataBuilder(JavaContext context, MetadataLoader loader, MetadataParser parser, MetaclassReader reader) {
		super();
		this.context = Objects.requireNonNull(context, "'context' cannot be null");
		this.loader = Objects.requireNonNull(loader, "'loader' cannot be null");
		this.parser = Objects.requireNonNull(parser, "'parser' cannot be null");
		this.reader = Objects.requireNonNull(reader, "'reader' cannot be null");
	}

	/**
	 * Simples Constructos. Default context, loader, parser and reader.
	 */
	public MetadataBuilder() {
		this(new BasicContext(), new BasicMetadataLoader(), new BasicMetadataParser(), new BasicMetaclassReader());
	}

	// **************************************************
	// Build
	// **************************************************

	public Metadata build(Object source) {
		if (source instanceof PplString) {
			return buildFromPpl(((PplString) source));
		}
		if (source instanceof Layout) {
			return buildFromLayout(source);
		}
		return buildFromObject(source);
	}

	public StaticMetadata buildStatic(Object source) {
		Metadata metadata = build(source);
		if (metadata instanceof StaticMetadata) {
			return (StaticMetadata) metadata;
		}
		throw new PplException(METADATA_IS_NOT_STATIC + metadata.toTree(0));
	}

	protected Metadata buildFromLayout(Object source) {
		return createFromLayout((Layout) source);
	}

	protected Metadata createFromLayout(Layout layout) {
		MetaInfo metaInfo = layout.metaInfo();
		List<Metadata> children = null;
		if (layout.hasChildren()) {
			children = new ArrayList<>();
			for (Layout child : layout.getChildren()) {
				children.add(createFromLayout(child));
			}
		}
		return context.metadataFactory().create(metaInfo, children);
	}

	protected Metadata buildFromPpl(PplString pplString) {
		return parser.parse(pplString);
	}

	protected Metadata buildFromObject(Object source) {
		if (source instanceof Collection<?>) {
			Collection<?> collection = (Collection<?>) source;
			if (collection.isEmpty()) {
				return buildFromPpl(PplString.Companion.getEMPTY());
			}
			return loader.load(source, reader.read(source.getClass(), collection.iterator().next().getClass()));
		}
		return loader.load(source, reader.read(source.getClass()));
	}

	// **************************************************
	// Get
	// **************************************************

	public JavaContext getContext() {
		return context;
	}

	public MetadataLoader getLoader() {
		return loader;
	}

	public MetadataParser getParser() {
		return parser;
	}

	public MetaclassReader getReader() {
		return reader;
	}

}
