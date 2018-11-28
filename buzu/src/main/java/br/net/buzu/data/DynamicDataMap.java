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
package br.net.buzu.data;

import br.net.buzu.pplspec.data.Data;
import br.net.buzu.pplspec.data.DataFactory;
import br.net.buzu.pplspec.data.DataMap;
import br.net.buzu.pplspec.lang.Token;
import br.net.buzu.pplspec.model.Metadata;

import java.util.Map;
import java.util.TreeMap;

/**
 * TODO
 * 
 * @author Douglas Siviotti
 * @since 1.0
 *
 */
public class DynamicDataMap implements DataMap {

	private final Map<String, Data<?>> map = new TreeMap<>();
	private final DataFactory dataFactory;
	private final Metadata metadata;

	public DynamicDataMap(Metadata metadata, DataFactory dataFactory) {
		super();
		this.metadata = metadata;
		this.dataFactory = dataFactory;
		create(null, metadata);
	}

	private void create(String parentId, Metadata m) {
		String id = parentId == null ? m.name() : parentId + Token.PATH_SEP + m.name();
		if (m.hasChildren()){
			for (Metadata child: m.children()){
				create(id, child);
			}
		} else {
			createAndPut(id, dataFactory.create(m));
		}
	}

	@Override
	public Data<?> get(String id) {
		if (!map.containsKey(id)) {
			throw new IllegalArgumentException("Data not found:" + id);
		}
		return map.get(id);
	}

	@Override
	public <T> T set(String id, T value) {
		if (!exists(id)) {
			createAndPut(id, dataFactory.createFromValue(value));
		}
		get(id).setValue(value);
		return value;
	}

	protected void createAndPut(String id, Data<?> data) {
		map.put(id, data);
	}

	@Override
	public boolean exists(String id) {
		return map.containsKey(id);
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		map.forEach((k,v)->sb.append(k).append("=").append(v).append("\n"));
		return sb.toString();
	}

	public Metadata getMetadata() {
		return metadata;
	}
	
}
