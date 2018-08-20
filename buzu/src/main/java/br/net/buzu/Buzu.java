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
package br.net.buzu;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import br.net.buzu.context.BasicContext;
import br.net.buzu.metaclass.BasicMetaclassReader;
import br.net.buzu.metadata.build.load.BasicMetadataLoader;
import br.net.buzu.metadata.build.parse.BasicMetadataParser;
import br.net.buzu.pplspec.api.MetaclassReader;
import br.net.buzu.pplspec.api.MetadataCoder;
import br.net.buzu.pplspec.api.MetadataLoader;
import br.net.buzu.pplspec.api.MetadataParser;
import br.net.buzu.pplspec.api.PayloadParser;
import br.net.buzu.pplspec.api.PplMapper;
import br.net.buzu.pplspec.context.PplContext;
import br.net.buzu.pplspec.exception.PplParseException;
import br.net.buzu.pplspec.model.Dialect;
import br.net.buzu.pplspec.model.Metaclass;
import br.net.buzu.pplspec.model.Metadata;
import br.net.buzu.pplspec.model.PplString;
import br.net.buzu.pplspec.model.StaticMetadata;

/**
 * Buzu Façade class. Basic implementation of <code>PplMapper</code>.
 * 
 * @author Douglas Siviotti
 * @since 1.0
 * @see PplMapper
 * @see BuzuBuilder
 */
public class Buzu implements PplMapper {

	static final String PARSE_REQUIRES_STATIC_METADATA = "The 'parse' operation (fromPPL) requires a StaticMetadata";
	private final PplContext context;
	private final MetadataParser parser;
	private final MetadataLoader loader;
	private final MetaclassReader reader;
	private final boolean serializeNulls;
	private final MetadataCoder coder;
	private final Dialect dialect;

	/**
	 * Simple constructor.
	 * 
	 */
	public Buzu() {
		this(new BasicContext());
	}

	/**
	 * Context constructor.
	 * 
	 * @param context The PPL Context [CANNOT BE NULL].
	 */
	public Buzu(PplContext context) {
		this(context, new BasicMetadataParser(context), new BasicMetaclassReader(context),
				new BasicMetadataLoader(context), Dialect.DEFAULT, false);
	}

	/**
	 * Complete Constructor (used by the Builder).
	 * 
	 * @param context         The PPL Context [CANNOT BE NULL].
	 * @param metadataParser  The MetadataParser. If <code>null</code> will be
	 *                        created an instance based on the context.
	 * @param metaclassReader The MetaclassLoader. If <code>null</code> will be
	 *                        created an instance based on the context.
	 * @param metadataLoader  The MetadataLoader. If <code>null</code> will be
	 *                        created an instance based on the context.
	 * 
	 */
	Buzu(PplContext context, MetadataParser metadataParser, MetaclassReader metaclassReader,
			MetadataLoader metadataLoader, Dialect dialect, boolean serializeNulls) {
		super();
		this.context = Objects.requireNonNull(context, "'context' cannot be null!");
		this.parser = metadataParser == null ? new BasicMetadataParser(context) : metadataParser;
		this.reader = metaclassReader == null ? new BasicMetaclassReader(context) : metaclassReader;
		this.loader = metadataLoader == null ? new BasicMetadataLoader(context) : metadataLoader;
		this.serializeNulls = serializeNulls;
		this.dialect = dialect != null ? dialect : Dialect.DEFAULT;
		this.coder = context.coderManager().get(this.dialect);
	}

	// **************************************************
	// API TEXT->OBJECT
	// **************************************************

	@Override
	@SuppressWarnings("unchecked")
	public Map<String, Object> fromPpl(String text) {
		return (Map<String, Object>) fromPpl(text, Map.class);
	}

	@Override
	public <T> T fromPpl(String text, Class<T> toClass) {
		if (text == null || text.isEmpty()) {
			return null;
		}

		PplString pplString = new PplString(text, context.syntax());
		Metadata metadata = parser.parse(pplString);
		Metaclass metaclass;
		if (metadata.kind().isMultiple()) {
			metaclass = readMetaclass(List.class, toClass);
		} else {
			metaclass = readMetaclass(toClass);
		}
		return fromPayload(asStatic(metadata), pplString.getPayload(), metaclass);
	}

	@Override
	public <T> List<T> fromPplList(String text, Class<T> elementType) {
		return fromPpl(text, readMetaclass(List.class, elementType));
	}

	@Override
	public <T> T fromPpl(String text, Metaclass toClass) {
		if (text == null || text.isEmpty()) {
			return null;
		}
		PplString pplString = new PplString(text, context.syntax());
		Metadata metadata = parser.parse(pplString);
		return fromPayload(asStatic(metadata), pplString.getPayload(), toClass);
	}

	private StaticMetadata asStatic(Metadata metadata) {
		if (!(metadata instanceof StaticMetadata)) {
			throw new PplParseException(PARSE_REQUIRES_STATIC_METADATA);
		}
		return (StaticMetadata) metadata;
	}

	@Override
	public <T> T fromPayload(StaticMetadata metadata, String payload, Metaclass toClass) {
		return context.parserFactory().create(toClass).parse(metadata, payload, toClass);
	}

	// **************************************************
	// API OBJECT->TEXT
	// **************************************************

	@Override
	public String toPpl(Object source) {
		if (source == null) {
			return null;
		}
		if (source instanceof Collection<?>) {
			return toPplFromCollection((Collection<?>) source);
		}
		return toPpl(source, readMetaclass(source.getClass()));
	}

	public String toPplFromCollection(Collection<?> source) {
		if (source.isEmpty()) {
			return PplString.EMPTY.getMetadata();
		}
		return toPpl(source, readMetaclass(source.getClass(), source.iterator().next().getClass()));
	}

	@Override
	public String toPpl(Object source, Metaclass fromClass) {
		if (fromClass instanceof StaticMetadata) {
			return toPpl((StaticMetadata) fromClass, source, fromClass);
		}
		return toPpl(asStatic(loader.load(source, fromClass)), source, fromClass);
	}

	@Override
	public String toPpl(StaticMetadata metadata, Object source, Metaclass fromClass) {
		PayloadParser payloadParser = context.parserFactory().create(fromClass);
		return context.syntax().asPplString(coder.code(metadata),
				payloadParser.serialize((StaticMetadata) metadata, source, fromClass));
	}

	private Metaclass readMetaclass(Class<?> fieldType) {
		return reader.read(fieldType, fieldType);
	}

	private Metaclass readMetaclass(Class<?> fieldType, Class<?> elementType) {
		return reader.read(fieldType, elementType);
	}

	// **************************************************
	// get/set
	// **************************************************

	public PplContext context() {
		return context;
	}

	public MetadataParser parser() {
		return parser;
	}

	public MetadataLoader loader() {
		return loader;
	}

	public MetaclassReader reader() {
		return reader;
	}

	public Dialect dialect() {
		return dialect;
	}

	public boolean isSerializaNulls() {
		return serializeNulls;
	}

	public MetadataCoder coder() {
		return coder;
	}

}
