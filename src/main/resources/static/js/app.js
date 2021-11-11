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
var appctx = $("meta[name='appctx']").attr("content");

const yasgui = new Yasgui(document.getElementById("yasgui"), {
  requestConfig: { endpoint: "https://apps.lifs-tools.org/goslin-sparql-dev/sparql/" },
  copyEndpointOnNewTab: false,
});
const query = `PREFIX goslin: <https://identifiers.org/lipids/goslin/> 
PREFIX grammar: <https://identifiers.org/lipids/goslin/grammar/>
SELECT ?string 
WHERE { [] goslin:className ?string ;
           grammar:any 'Cer(d18:1/20:2)' . }`;
yasgui.getTab().yasqe.setValue(query);

