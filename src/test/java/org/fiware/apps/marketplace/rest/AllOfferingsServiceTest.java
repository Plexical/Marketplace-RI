package org.fiware.apps.marketplace.rest;

/*
 * #%L
 * FiwareMarketplace
 * %%
 * Copyright (C) 2014 CoNWeT Lab, Universidad Politécnica de Madrid
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

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.core.Response;

import org.fiware.apps.marketplace.bo.ServiceBo;
import org.fiware.apps.marketplace.model.ErrorType;
import org.fiware.apps.marketplace.model.Service;
import org.fiware.apps.marketplace.model.Services;
import org.fiware.apps.marketplace.security.auth.OfferingRegistrationAuth;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class AllOfferingsServiceTest {
	
	@Mock private ServiceBo serviceBoMock;
	@Mock private OfferingRegistrationAuth offeringRegistrationAuthMock;
	
	@InjectMocks private AllOfferingsService allOfferingsService;
	
	private static final String OFFSET_MAX_INVALID = "offset and/or max are not valid";
	
	@Before 
	public void initMocks() {
		MockitoAnnotations.initMocks(this);
	}

	@Test
	public void testListAllServicesNotAllowed() {
		// Mocks
		when(offeringRegistrationAuthMock.canList()).thenReturn(false);

		// Call the method
		Response res = allOfferingsService.listServices(0, 100);

		// Assertions
		GenericRestTestUtils.checkAPIError(res, 401, ErrorType.UNAUTHORIZED, "You are not authorized to list offerings");
	}
	
	
	private void testListAllServicesInvalidParams(int offset, int max) {
		// Mocks
		when(offeringRegistrationAuthMock.canList()).thenReturn(true);

		// Call the method
		Response res = allOfferingsService.listServices(offset, max);

		// Assertions
		GenericRestTestUtils.checkAPIError(res, 400, ErrorType.BAD_REQUEST, OFFSET_MAX_INVALID);
	}
	
	@Test
	public void testListAllServicesInvalidOffset() {
		testListAllServicesInvalidParams(-1, 100);
	}
	
	@Test
	public void testListAllServicesInvalidMax() {
		testListAllServicesInvalidParams(0, -1);
	}
	
	@Test
	public void testListAllServicesInvalidOffsetMax() {
		testListAllServicesInvalidParams(-1, -1);
	}
	
	@Test
	public void testListAllServicesGetNoErrors() {
		List<Service> services = new ArrayList<Service>();
		for (int i = 0; i < 3; i++) {
			Service service = new Service();
			service.setId(i);
			services.add(service);
		}
		
		// Mocks
		when(offeringRegistrationAuthMock.canList()).thenReturn(true);
		when(serviceBoMock.getServicesPage(anyInt(), anyInt())).thenReturn(services);
		
		// Call the method
		int offset = 0;
		int max = 100;
		Response res = allOfferingsService.listServices(offset, max);
		
		// Verify
		verify(serviceBoMock).getServicesPage(offset, max);
		
		// Assertions
		assertThat(res.getStatus()).isEqualTo(200);
		assertThat(((Services) res.getEntity()).getServices()).isEqualTo(services);
	}
	
	@Test
	public void testListAllServicesException() {
		// Mocks
		String exceptionMsg = "exception";
		doThrow(new RuntimeException("", new Exception(exceptionMsg))).when(serviceBoMock).getServicesPage(anyInt(), anyInt());
		when(offeringRegistrationAuthMock.canList()).thenReturn(true);

		// Call the method
		int offset = 0;
		int max = 100;
		Response res = allOfferingsService.listServices(offset, max);
		
		// Verify
		verify(serviceBoMock).getServicesPage(offset, max);
		
		// Check exception
		GenericRestTestUtils.checkAPIError(res, 500, ErrorType.INTERNAL_SERVER_ERROR, exceptionMsg);
	}
}
