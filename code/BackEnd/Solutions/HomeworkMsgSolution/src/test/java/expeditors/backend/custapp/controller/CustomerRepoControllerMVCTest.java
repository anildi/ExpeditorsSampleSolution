package expeditors.backend.custapp.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import expeditors.backend.custapp.domain.Customer;
import expeditors.backend.custapp.domain.PhoneNumber;
import expeditors.backend.custapp.sql.ScriptFileProperties;
import expeditors.backend.custapp.sql.SqlScriptBase;
import jakarta.annotation.PostConstruct;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.http.MediaType;
import org.springframework.jdbc.datasource.init.ScriptUtils;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author whynot
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
@WithMockUser(roles = "ADMIN")
public class CustomerRepoControllerMVCTest extends SqlScriptBase {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper mapper;

    private String baseUrl;
    private String rootUrl;
    private String oneCustomerUrl;
    private String custWithPhonesUrl;

    @Autowired
    private DataSource dataSource;

    @Autowired
    private ScriptFileProperties props;

    @PostConstruct
    public void uberInit() {
//        baseUrl = "http://localhost:" + port;
        rootUrl = "/customerrepo";
        oneCustomerUrl = rootUrl + "/{id}";
        custWithPhonesUrl = rootUrl + "/{id}?phones=true";

    }

    @Autowired
    private ApplicationContext context;
    @BeforeEach
    public void init() throws SQLException {
    }

    @Test
    public void testGetAll() throws Exception {
        MockHttpServletRequestBuilder builder = get(rootUrl)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON);

        ResultActions actions = mockMvc.perform(builder)
                .andExpect(status().isOk());

    }

    @Test
    public void testGetOneWithGoodId() throws Exception {
        MockHttpServletRequestBuilder builder = get(oneCustomerUrl, 1)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON);

        ResultActions actions = mockMvc.perform(builder)
                .andExpect(status().isOk());
    }

    @Test
    public void testGetOneWithBadId() throws Exception {
        MockHttpServletRequestBuilder builder = get(oneCustomerUrl, 1000)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON);

        ResultActions actions = mockMvc.perform(builder)
                .andExpect(status().isNotFound());
    }

    @Test
    public void testPostCustomer() throws Exception {
        Customer c = new Customer("New Customer", LocalDate.of(1960, 6, 9),
                Customer.Status.NORMAL, new PhoneNumber("999 99 9999", PhoneNumber.Type.WORK));
        String jsonString = mapper.writeValueAsString(c);

        ResultActions actions = mockMvc.perform(post(rootUrl)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonString));

        actions = actions.andExpect(status().isCreated());

        MvcResult result = actions.andReturn();
        String locHeader = result.getResponse().getHeader("Location");
        assertNotNull(locHeader);
        System.out.println("locHeader: " + locHeader);
    }

    @Test
    public void testDeleteCustomerWithGoodId() throws Exception {
        ResultActions actions = mockMvc.perform(delete(oneCustomerUrl, 1)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON));

        actions.andExpect(status().isNoContent());
    }

    @Test
    public void testDeleteCustomerWithBadId() throws Exception {
        ResultActions actions = mockMvc.perform(delete(oneCustomerUrl, 1000)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON));

        actions.andExpect(status().isNotFound());
    }

    @Test
    public void testUpdateCustomerWithGoodId() throws Exception {
        MockHttpServletRequestBuilder builder = get(oneCustomerUrl, 1)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON);

        ResultActions actions = mockMvc.perform(builder)
                .andExpect(status().isOk());
        MvcResult result = actions.andReturn();

        String jsonResult = result.getResponse().getContentAsString();
        JsonNode node = mapper.readTree(jsonResult);
        Customer customer = mapper.treeToValue(node, Customer.class);
        customer.setName("Blech");

        String updatedJson = mapper.writeValueAsString(customer);

        actions = mockMvc.perform(put(rootUrl)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(updatedJson));

        actions = actions.andExpect(status().isNoContent());

        builder = get(oneCustomerUrl, 1)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON);

        actions = mockMvc.perform(builder)
                .andExpect(status().isOk());
        result = actions.andReturn();

        jsonResult = result.getResponse().getContentAsString();
        node = mapper.readTree(jsonResult);
        customer = mapper.treeToValue(node, Customer.class);

        assertEquals("Blech", customer.getName());
    }

    @Test
    public void testUpdateCustomerWithBadId() throws Exception {
        MockHttpServletRequestBuilder builder = get(oneCustomerUrl, 1)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON);

        ResultActions actions = mockMvc.perform(builder)
                .andExpect(status().isOk());
        MvcResult result = actions.andReturn();

        String jsonResult = result.getResponse().getContentAsString();
        JsonNode node = mapper.readTree(jsonResult);
        Customer customer = mapper.treeToValue(node, Customer.class);
        customer.setName("Blech");
        customer.setId(1000);

        String updatedJson = mapper.writeValueAsString(customer);

        actions = mockMvc.perform(put(rootUrl)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(updatedJson));

        actions = actions.andExpect(status().isNotFound());

    }

    @Test
    public void testAddPhoneNumberToExistingCustomer() throws Exception {
        String number = "3839 929 92922";
        String type = "HOME";
        String addPhoneUrl = oneCustomerUrl + "/{phone}/{type}";

        MockHttpServletRequestBuilder builder = post(addPhoneUrl, 1, number, type)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON);

        ResultActions actions = mockMvc.perform(builder)
                .andExpect(status().isNoContent());

       builder = get(custWithPhonesUrl, 1, true)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON);

        actions = mockMvc.perform(builder)
                .andExpect(status().isOk());
        MvcResult result = actions.andReturn();

        String jsonResult = result.getResponse().getContentAsString();
        JsonNode node = mapper.readTree(jsonResult);
        Customer customer = mapper.treeToValue(node, Customer.class);

        assertEquals(3, customer.getPhoneNumbers().size());
    }

    @Test
    public void testDeletePhoneNumberFromExistingCustomer() throws Exception {
        String number = "222 333-5555";
        String type = "WORK";
        String phoneUrl = oneCustomerUrl + "/{phone}/{type}";

        MockHttpServletRequestBuilder builder = delete(phoneUrl, 1, number, type)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON);

        ResultActions actions = mockMvc.perform(builder)
                .andExpect(status().isNoContent());

        builder = get(custWithPhonesUrl, 1, true)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON);

        actions = mockMvc.perform(builder)
                .andExpect(status().isOk());
        MvcResult result = actions.andReturn();

        String jsonResult = result.getResponse().getContentAsString();
        JsonNode node = mapper.readTree(jsonResult);
        Customer customer = mapper.treeToValue(node, Customer.class);

        assertEquals(1, customer.getPhoneNumbers().size());
    }
}
