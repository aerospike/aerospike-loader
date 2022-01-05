/*******************************************************************************
 * Copyright 2022 by Aerospike.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to
 * deal in the Software without restriction, including without limitation the
 * rights to use, copy, modify, merge, publish, distribute, sublicense, and/or
 * sell copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING
 * FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS
 * IN THE SOFTWARE.
 ******************************************************************************/

package com.aerospike.load;

import java.util.List;

/**
 * 
 * @author Aerospike
 * 
 * MappinfDefinition (Key, Set, Bin definition) populated by parsing config file.
 * 
 */
public class MappingDefinition {
	public boolean secondaryMapping = false;
	public MetaDefinition keyColumnDef;
	public MetaDefinition setColumnDef;
	public List<BinDefinition> binColumnDefs;

	public MappingDefinition(boolean secondary_mapping, MetaDefinition keyColumnDef, MetaDefinition setColumnDef, List<BinDefinition> binColumnDefs) {
		super();
		this.secondaryMapping = secondary_mapping;
		this.keyColumnDef = keyColumnDef;
		this.setColumnDef = setColumnDef;
		this.binColumnDefs = binColumnDefs;
	}

	@Override
	public String toString() {
		return "MappingDefinition [secondary_mapping=" + this.secondaryMapping + " keyColumnDef=" + this.keyColumnDef
				+ " setColumnDef=" + this.setColumnDef + "binColumnDefs=" + this.binColumnDefs + "]";
	}

}
