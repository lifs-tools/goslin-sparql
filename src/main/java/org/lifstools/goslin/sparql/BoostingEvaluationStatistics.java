/*
 * Copyright 2021 Lipidomics Informatics for Life Sciences.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.lifstools.goslin.sparql;

import org.eclipse.rdf4j.query.algebra.StatementPattern;
import org.eclipse.rdf4j.query.algebra.TupleExpr;
import org.eclipse.rdf4j.query.algebra.evaluation.impl.EvaluationStatistics;

/**
 * This implementation allows to adjust the cardinality of terms that stem from the grammar
 * namespace to perform parsing steps before any further steps are executed.
 *
 * @author nilshoffmann
 */
public class BoostingEvaluationStatistics extends EvaluationStatistics {

    protected static class BoostingCardinalityCalculator extends CardinalityCalculator {

        @Override
        public double getSubjectCardinality(StatementPattern sp) {
            return super.getSubjectCardinality(sp);
        }

        @Override
        public double getPredicateCardinality(StatementPattern sp) {
            return super.getPredicateCardinality(sp);
        }

        @Override
        public double getObjectCardinality(StatementPattern sp) {
            return super.getObjectCardinality(sp);
        }

        @Override
        public double getContextCardinality(StatementPattern sp) {
            return super.getContextCardinality(sp);
        }

    }

    @Override
    protected CardinalityCalculator createCardinalityCalculator() {
        return new BoostingCardinalityCalculator();
    }

    protected BoostingCardinalityCalculator getCardinalityCalculator() {
        return (BoostingCardinalityCalculator) cc;
    }

    @Override
    public double getCardinality(TupleExpr expr) {
        if (expr instanceof StatementPattern sp) {
            if (sp.getSubjectVar() == null && sp.getObjectVar().getValue().isIRI()) {
                String iri = sp.getObjectVar().getValue().stringValue();
                // make grammar statements that trigger a parse cheaper -> use the VAR_CARDINALITY value of 10 in CardinalityCalculator
                if (iri.startsWith(GoslinConstants.LIPID_GRAMMAR)) {
                    return 10;
                } else {
                    // make other statements that depend on the parse more expensive so that they are optimized to execute later
                    return 2 * getCardinalityCalculator().getSubjectCardinality(sp) * getCardinalityCalculator().getPredicateCardinality(sp) * getCardinalityCalculator().getObjectCardinality(sp)
                            * getCardinalityCalculator().getContextCardinality(sp);
                }
            }
        }
        return super.getCardinality(expr);
    }
}
