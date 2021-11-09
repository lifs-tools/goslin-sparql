package org.lifstools.goslin.sparql;

import org.eclipse.rdf4j.model.impl.SimpleBNode;
import org.lifstools.jgoslin.domain.LipidAdduct;


public class LipidWrappingBnode extends SimpleBNode {
	private final LipidAdduct lipid;

	public LipidWrappingBnode(LipidAdduct lipidd) {
		super();
		this.lipid = lipidd;
	}

	public LipidAdduct getLipidAdduct() {
		return lipid;
	}

}
