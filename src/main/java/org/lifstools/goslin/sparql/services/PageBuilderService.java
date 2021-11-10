/*
 * Copyright 2021 Leibniz Institut für Analytische Wissenschaften - ISAS e.V..
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
package org.lifstools.goslin.sparql.services;

import de.isas.lifs.webapps.common.domain.DefaultAppInfo;
import de.isas.lifs.webapps.common.domain.DefaultPage;
import java.security.Principal;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

/**
 *
 * @author nilshoffmann
 */
@Service
public class PageBuilderService {

    private final DefaultAppInfo appInfo;

    @Autowired
    public PageBuilderService(DefaultAppInfo appInfo) {
        this.appInfo = appInfo;
    }

    public DefaultPage createPage(String title) {
        return DefaultPage.builder().title(title).
                appVersion(appInfo.getVersionNumber()).
                authServerBaseUrl(appInfo.getAuthServerBaseUrl()).
                authServerRealm(appInfo.getAuthServerRealm()).
                author(appInfo.getToolAuthor()).
                buildDate(appInfo.getBuildDate()).
                description(appInfo.getToolDescription()).
                gaId(appInfo.getGaId()).
                scmBranch(appInfo.getScmBranch()).
                scmCommitId(appInfo.getScmCommitId()).
                scmUrl(appInfo.getScmUrl()).
                supportUrl(appInfo.getSupportUrl()).
                toolAuthor(appInfo.getToolAuthor()).
                toolContact(appInfo.getToolContact()).
                toolDescription(appInfo.getToolDescription()).
                toolLicense(appInfo.getToolLicense()).
                toolLicenseUrl(appInfo.getToolLicenseUrl()).
                toolTitle(appInfo.getToolTitle()).
                toolUrl(appInfo.getToolUrl()).
                toolVersionNumber(appInfo.getToolVersionNumber()).
                build();
    }
}
