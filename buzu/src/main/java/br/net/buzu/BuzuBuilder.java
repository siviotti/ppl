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
package br.net.buzu;

import br.net.buzu.context.ContextBuilder;
import br.net.buzu.metaclass.AnnotationSkipStrategy;
import br.net.buzu.metaclass.BasicMetaclassReader;
import br.net.buzu.metaclass.BasicSkipStrategy;
import br.net.buzu.metadata.build.load.BasicMetadataLoader;
import br.net.buzu.metadata.build.parse.BasicMetadataParser;
import br.net.buzu.metadata.build.parse.Splitter;
import br.net.buzu.pplspec.api.MetaclassReader;
import br.net.buzu.pplspec.api.MetadataLoader;
import br.net.buzu.pplspec.api.MetadataParser;
import br.net.buzu.pplspec.api.SkipStrategy;
import br.net.buzu.pplspec.context.PplContext;
import br.net.buzu.pplspec.lang.Syntax;
import br.net.buzu.pplspec.model.Dialect;

import java.lang.annotation.Annotation;
import java.util.HashSet;
import java.util.Set;

/**
 * Builder to specific PplMapper (Buzu).
 *
 * @author Douglas Siviotti
 * @since 1.0
 *
 */
public class BuzuBuilder {

	private Syntax syntax;
	private PplContext context;
	private Splitter splitter;
	private MetadataParser metadataParser;
	private SkipStrategy skipStrategy;
	private Set<Class<? extends Annotation>> ignoreAnnotations = new HashSet<>();
	private Set<Class<? extends Annotation>> useAnnotations = new HashSet<>();
	private MetaclassReader metaclassReader;
	private MetadataLoader metadataLoader;
	private Dialect dialect;
	private boolean serializaNulls;

	/**
	 * Build and returns a instance domainOf <code>Buzu</code>.
	 * 
	 * @return The instance domainOf <code>Buzu</code> created based on internal
	 *         objects.
	 */
	public Buzu build() {
		if (syntax == null) {
			syntax = new Syntax();
		}
		if (context == null) {
			context = new ContextBuilder().syntax(syntax).build();
		}
		if (splitter == null) {
			splitter = new Splitter(syntax);
		}
		if (metadataParser == null) {
			metadataParser = new BasicMetadataParser(context, splitter);
		}
		if (skipStrategy == null) {
			skipStrategy = new BasicSkipStrategy();
		}
		if (useCustomSkipStrategy()) {
			skipStrategy = new AnnotationSkipStrategy(ignoreAnnotations, useAnnotations, skipStrategy);
		}
		if (metaclassReader == null) {
			metaclassReader = new BasicMetaclassReader(context, skipStrategy);
		}
		if (metadataLoader == null) {
			metadataLoader = new BasicMetadataLoader(context);
		}
		if (dialect == null) {
			dialect = Buzu.DEFAULT_DIALECT;
		}
		return new Buzu(context, metadataParser, metaclassReader, metadataLoader, dialect, serializaNulls);
	}
	
	private boolean useCustomSkipStrategy(){
		return !ignoreAnnotations.isEmpty() || !useAnnotations.isEmpty();
	}

	public Syntax getSyntax() {
		return syntax;
	}

	public BuzuBuilder syntax(Syntax syntax) {
		this.syntax = syntax;
		return this;
	}

	public PplContext getContext() {
		return context;
	}

	public BuzuBuilder context(PplContext context) {
		this.context = context;
		return this;
	}

	public MetadataParser getMetadataParser() {
		return metadataParser;
	}

	public BuzuBuilder metadataParser(MetadataParser metadataParser) {
		this.metadataParser = metadataParser;
		return this;
	}

	public Splitter getSplitter() {
		return splitter;
	}

	public BuzuBuilder splitter(Splitter splitter) {
		this.splitter = splitter;
		return this;
	}

	public MetadataLoader getMetadataLoader() {
		return metadataLoader;
	}

	public BuzuBuilder metadataLoader(MetadataLoader metadataLoader) {
		this.metadataLoader = metadataLoader;
		return this;
	}

	public MetaclassReader getMetaclassReader() {
		return metaclassReader;
	}

	public BuzuBuilder metaclassReader(MetaclassReader metaclassLoader) {
		this.metaclassReader = metaclassLoader;
		return this;
	}

	public SkipStrategy getSkipStrategy() {
		return skipStrategy;
	}

	public BuzuBuilder skipStrategy(SkipStrategy skip) {
		this.skipStrategy = skip;
		return this;
	}

	public BuzuBuilder addIgnore(Class<? extends Annotation> annotation) {
		ignoreAnnotations.add(annotation);
		return this;
	}

	public BuzuBuilder addUse(Class<? extends Annotation> annotation) {
		useAnnotations.add(annotation);
		return this;
	}

	public Dialect getDialect() {
		return dialect;
	}

	public BuzuBuilder dialect(Dialect dialect) {
		this.dialect = dialect;
		return this;
	}

	public boolean isSerializaNulls() {
		return serializaNulls;
	}

	public BuzuBuilder serializaNulls(boolean serializaNulls) {
		this.serializaNulls = serializaNulls;
		return this;
	}

}
