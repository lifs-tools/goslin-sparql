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
import static org.lifstools.goslin.sparql.GoslinConstants.IRI_PREFIX;

/**
 * These IRIs are not authoritative yet. Identifiers.org does not have a
 * lipid(s) prefix yet.
 *
 * @author nilshoffmann
 */
public enum GoslinPredicate {

    //Subjects
    SWISS_LIPIDS(IRI_PREFIX, "swisslipids"),
    LIPID_MAPS(IRI_PREFIX, "lipidmaps"),
    HMDB(IRI_PREFIX, "hmdb"),
    GOSLIN(IRI_PREFIX, "goslin"),
    SHORTHAND_2020(IRI_PREFIX, "shorthand2020"),
    ANY_GRAMMAR(IRI_PREFIX, "any"),
    //Predicates
    LIPID_CATEGORY_NAME(IRI_PREFIX, "lipidCategoryName"),
    LIPID_CLASS_NAME(IRI_PREFIX, "lipidClassName"),
    LIPID_SPECIES_NAME(IRI_PREFIX, "lipidSpeciesName"),
    LIPID_MOLECULAR_SPECIES_NAME(IRI_PREFIX, "lipidMolecularSpeciesName"),
    LIPID_SN_POSITION_NAME(IRI_PREFIX, "lipidSnPositionName"),
    LIPID_STRUCTURE_DEFINED_NAME(IRI_PREFIX, "lipidStructureDefinedName"),
    LIPID_FULL_STRUCTURE_NAME(IRI_PREFIX, "lipidFullStructureName"),
    LIPID_COMPLETE_STRUCTURE_NAME(IRI_PREFIX, "lipidCompleteStructureName"),
    LIPID_PARENTS(IRI_PREFIX, "parents");

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
