package org.fiware.apps.marketplace.bo.impl;

/*
 * #%L
 * FiwareMarketplace
 * %%
 * Copyright (C) 2012 SAP
 * %%
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 * 
 * 1. Redistributions of source code must retain the above copyright notice,
 *    this list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 * 3. Neither the name of copyright holders nor the names of its contributors
 *    may be used to endorse or promote products derived from this software 
 *    without specific prior written permission.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDERS OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 * #L%
 */

import java.util.HashMap;

import org.fiware.apps.marketplace.bo.AttributeTypeStatisticsBo;
import org.fiware.apps.marketplace.model.ServiceAttributeTypeStatistics;
import org.springframework.stereotype.Service;

@Service("attributeTypeStatisticsBo")
public class AttributeTypeStatisticsBoImpl implements AttributeTypeStatisticsBo {

	private HashMap<String, ServiceAttributeTypeStatistics> typeProbabilities;
	
	public AttributeTypeStatisticsBoImpl() {
		typeProbabilities = new HashMap<String, ServiceAttributeTypeStatistics>();
	}
	
	@Override
	public void save(String attributeTypeUri, ServiceAttributeTypeStatistics probabilityOfOccurence) {
		if(typeProbabilities.containsKey(attributeTypeUri))
			return;
		typeProbabilities.put(attributeTypeUri, probabilityOfOccurence);
	}

	@Override
	public void delete(String attributeTypeUri) {
		if(typeProbabilities.containsKey(attributeTypeUri))
			typeProbabilities.remove(attributeTypeUri);
	}

	@Override
	public ServiceAttributeTypeStatistics getByUri(String attributeTypeUri) {
		if(typeProbabilities.containsKey(attributeTypeUri))
			return typeProbabilities.get(attributeTypeUri);
		return null;
	}

	@Override
	public HashMap<String, ServiceAttributeTypeStatistics> getAllAttributeTypeStatistics() {
		return typeProbabilities;
	}
}
