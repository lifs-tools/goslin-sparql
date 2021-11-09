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

import org.eclipse.rdf4j.model.IRI;
import static org.eclipse.rdf4j.model.util.Values.iri;

/**
 *
 * @author nilshoffmann
 */
public class GoslinIri {

    public static final IRI SWISS_LIPIDS = iri("https://identifiers.org/lipids/nomenclature/swisslipids");
    public static final IRI LIPID_MAPS = iri("https://identifiers.org/lipids/nomenclature/lipidmaps");
    public static final IRI HMDB = iri("https://identifiers.org/lipids/nomenclature/hmdb");
    public static final IRI GOSLIN = iri("https://identifiers.org/lipids/nomenclature/goslin");
    public static final IRI SHORTHAND_2020 = iri("https://identifiers.org/lipids/nomenclature/shorthand2020");
}
