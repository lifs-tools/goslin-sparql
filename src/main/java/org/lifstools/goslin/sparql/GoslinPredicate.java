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

import java.util.Arrays;
import java.util.Optional;
import static org.lifstools.goslin.sparql.GoslinConstants.LIPID_NOMENCLATURE;

/**
 * These IRIs are not authoritative yet. Identifiers.org does not have a
 * lipid(s) prefix yet.
 *
 * @author nilshoffmann
 */
public enum GoslinPredicate {

    //Subjects
    SWISS_LIPIDS(LIPID_NOMENCLATURE, "swisslipids"),
    LIPID_MAPS(LIPID_NOMENCLATURE, "lipidmaps"),
    HMDB(LIPID_NOMENCLATURE, "hmdb"),
    GOSLIN(LIPID_NOMENCLATURE, "goslin"),
    SHORTHAND_2020(LIPID_NOMENCLATURE, "shorthand2020"),
    ANY_GRAMMAR(LIPID_NOMENCLATURE, "any"),
    //Predicates
    LIPID_CATEGORY_NAME(LIPID_NOMENCLATURE, "categoryName"),
    LIPID_CLASS_NAME(LIPID_NOMENCLATURE, "className"),
    LIPID_SPECIES_NAME(LIPID_NOMENCLATURE, "speciesName"),
    LIPID_MOLECULAR_SPECIES_NAME(LIPID_NOMENCLATURE, "molecularSpeciesName"),
    LIPID_SN_POSITION_NAME(LIPID_NOMENCLATURE, "snPositionName"),
    LIPID_STRUCTURE_DEFINED_NAME(LIPID_NOMENCLATURE, "structureDefinedName"),
    LIPID_FULL_STRUCTURE_NAME(LIPID_NOMENCLATURE, "fullStructureName"),
    LIPID_COMPLETE_STRUCTURE_NAME(LIPID_NOMENCLATURE, "completeStructureName"),
    LIPID_PARENT_CLASSES(LIPID_NOMENCLATURE, "parentClasses"),
    LIPID_SHORTHAND_NAME(LIPID_NOMENCLATURE, "shorthandName"),
    LIPID_SHORTHAND_CLASSIFICATION_LEVEL(LIPID_NOMENCLATURE, "shorthandClassificationLevel"),
    LIPID_SUM_FORMULA(LIPID_NOMENCLATURE, "sumFormula"),
    LIPID_MW(LIPID_NOMENCLATURE, "exactMass");

    private final String prefix;
    private final String identifier;

    private GoslinPredicate(String prefix, String identifier) {
        this.prefix = prefix;
        this.identifier = identifier;
    }

    public String getPrefix() {
        return prefix;
    }

    public String getIdentifier() {
        return identifier;
    }

    /**
     * Tries to find the corresponding subject or predicate by its local name.
     *
     * @param localName the local name of the subject or predicate.
     * @return the corresponding subject or predicate, if it exists. It not, an
     * empty optional will be returned.
     */
    public static Optional<GoslinPredicate> forLocalName(String localName) {
        return Arrays.asList(values()).stream().filter((element) -> {
            return element.getIdentifier().equalsIgnoreCase(localName.trim());
        }).findFirst();
    }

}
