/*
 * ###
 * Phresco Service Implemenation
 * 
 * Copyright (C) 1999 - 2012 Photon Infotech Inc.
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
 * ###
 */
/*******************************************************************************
 * Copyright (c) 2011 Photon.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Photon Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.photon.in/legal/ppl-v10.html
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * 
 * Contributors:
 *     Photon - initial API and implementation
 ******************************************************************************/
package com.photon.phresco.service.dependency.impl;

import java.io.File;
import java.util.List;

import javax.xml.bind.JAXBException;

import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;

import com.photon.phresco.exception.PhrescoException;
import com.photon.phresco.model.ModuleGroup;
import com.photon.phresco.model.ProjectInfo;
import com.photon.phresco.service.api.RepositoryManager;
import com.phresco.pom.exception.PhrescoPomException;

public class JWSDependencyProcessor extends AbstractDependencyProcessor {
	private static final Logger S_LOGGER = Logger.getLogger(JWSDependencyProcessor.class);
	private static Boolean isDebugEnabled = S_LOGGER.isDebugEnabled();
	public JWSDependencyProcessor(RepositoryManager repoManager) {
		super(repoManager);
	}

	@Override
	protected String getModulePathKey() {
		return null;
	}
	
	@Override
	public void process(ProjectInfo info, File path) throws PhrescoException {
		if (isDebugEnabled) {
			S_LOGGER.debug("Entering Method JWSDependencyProcessor.process(ProjectInfo info, File path)");
			S_LOGGER.debug("process() Path=" + path.getPath());
		}
		super.process(info, path);
		createSqlFolder(info, path);
		updateTestPom(path);
	}

	@Override
	protected void extractModules(File path, List<com.photon.phresco.model.ModuleGroup> modules) throws PhrescoException {
		if (isDebugEnabled) {
			S_LOGGER.debug("Entering Method JWSDependencyProcessor.extractModules(File path, List<TupleBean> modules)");
		}
		if(CollectionUtils.isEmpty(modules)) {
			return;
		}
		try {
			updatePOMWithModules(path, modules);
		} catch (JAXBException e) {
			throw new PhrescoException(e);
		} catch (PhrescoPomException e) {
			throw new PhrescoException(e);
		}
	}
}