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
package br.net.buzu.metadata.build.load;

import java.util.Arrays;
import java.util.List;

import br.net.buzu.context.BasicContext;
import br.net.buzu.pplspec.api.MetadataLoader;
import br.net.buzu.pplspec.context.PplContext;
import br.net.buzu.pplspec.exception.PplMetaclassViolationException;
import br.net.buzu.pplspec.model.MetaInfo;
import br.net.buzu.pplspec.model.Metaclass;
import br.net.buzu.pplspec.model.Metadata;

/**
 * Default MetadataLoader
 *
 * @author Douglas Siviotti
 * @since 1.0
 *
 */
public class BasicMetadataLoader implements MetadataLoader {

	private final PplContext context;

	public BasicMetadataLoader() {
		this(new BasicContext());
	}

	public BasicMetadataLoader(PplContext context) {
		super();
		if (context == null) {
			throw new IllegalArgumentException("Context cannot be null!");
		}
		this.context = context;
	}

	// ********** API **********

	@Override
	public Metadata load(Object instance, Metaclass metaclass) {
		LoadNode node = new LoadNode(instance, metaclass, "");
		MaxMap maxMap = new MaxMap();
		Max max = getMax(maxMap, node, metaclass.info());
		return createMetadata(metaclass.info().update(max.getMaxSize(), max.getMaxOccurs()), max,
				createChildren(node, maxMap));
	}

	// ********** INTERNAL **********

	private Metadata createMetadata(MetaInfo metaInfo, Max max, List<Metadata> children) {
		return context.metadataFactory().create(metaInfo, children);
	}

	private Max getMax(MaxMap maxMap, LoadNode node, MetaInfo metaInfo) {
		String fieldPath = node.getFieldPath();
		Max max = maxMap.get(fieldPath);
		int size = max.tryNewMaxSize(node.calcMaxSize()).getMaxSize();
		int maxOccurs = max.tryNewMaxOccurs(node.getOccurs()).getMaxOccurs();
		if (metaInfo.hasSize()) {
			checkLimit("size", fieldPath, metaInfo.getSize(), size);
		}
		if (metaInfo.hasMaxOccurs()) {
			checkLimit("maxOccurs", fieldPath, metaInfo.getMaxOccurs(), maxOccurs);
		}
		return max;
	}

	private List<Metadata> createChildren(LoadNode node, final MaxMap maxMap) {
		if (!node.isComplex() || node.isEnum()) {
			return null;
		}
		List<Metaclass> metaclassList = node.getMetaclass().children();
		Metadata[] children = new Metadata[metaclassList.size()];
		Object fieldValue;
		Metaclass childMetaclass;
		for (Object itemValue : node.getValue()) {
			for (int i = 0; i < metaclassList.size(); i++) {
				childMetaclass = metaclassList.get(i);
				fieldValue = itemValue != null ? childMetaclass.get(itemValue) : null;
				children[i] = loadChild(fieldValue, childMetaclass, node, maxMap);
			}
		}
		return Arrays.asList(children);
	}

	private Metadata loadChild(Object fieldValue, Metaclass metaclass, LoadNode parentNode, MaxMap maxMap) {
		String fieldPath = MaxMap.getFieldPath(metaclass, parentNode);
		LoadNode fieldNode = new LoadNode(fieldValue, metaclass, fieldPath);
		MetaInfo metaInfo = metaclass.info();
		Max max = getMax(maxMap, fieldNode, metaInfo);
		metaInfo = metaInfo.update(max.getMaxSize(), max.getMaxOccurs());
		return createMetadata(metaInfo, max, createChildren(fieldNode, maxMap));
	}

	private void checkLimit(String info, String fieldPath, int maxValue, int newValue) {
		if (newValue > maxValue) {
			StringBuilder sb = new StringBuilder();
			sb.append(info).append(" violation on field '").append(fieldPath).append("'. Max value:'").append(maxValue)
					.append("' calculated value:'").append(newValue).append("'.");
			throw new PplMetaclassViolationException(sb.toString());
		}
	}

}
