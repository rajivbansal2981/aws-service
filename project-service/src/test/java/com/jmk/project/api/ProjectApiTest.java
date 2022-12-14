package com.jmk.project.api;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jmk.enums.Status;
import com.jmk.project.model.Project;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ProjectApiTest {
	
	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	private List<Project> projects;
	
	private static final String BASE_URL="http://localhost:5379";
	
	private Project project;
	
	@Before
	public void setUp() {
		project=new Project();
		project.setName("Construction");
		project.setCode("DDC");
		project.setManager("Rajiv");
		project.setStatus(Status.A);
		project.setDescription("ProjectDescription");
		project.setStartDate(LocalDate.now());
		project.setCreatedOn(LocalDateTime.now());
		project.setWhenModified(LocalDateTime.now());
	}
	
	@Test
	public void testCreateProject() {
		
		String jsonProject=null;
		try {
			jsonProject=objectMapper.writeValueAsString(project);
			System.out.print("......"+jsonProject);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		try {
			MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post("http://localhost:5379/project/")
					.accept(MediaType.APPLICATION_JSON_VALUE).contentType(MediaType.APPLICATION_JSON_VALUE)
					.content(jsonProject)).andExpect(MockMvcResultMatchers.status().isOk()).andReturn();
			project=objectMapper.readValue(mvcResult.getResponse().getContentAsByteArray(), Project.class);
			Assert.assertNotNull(project);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void testFindProjectByStatus() {
		try {
			ResultActions resultActions=mockMvc.perform( MockMvcRequestBuilders
				      .get("http://localhost:5379/project/findByStatus").param("status","A")
				      .accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON_VALUE))
				          .andExpect(MockMvcResultMatchers.status().isOk());
			projects=(List<Project>)objectMapper.readValue(resultActions.andReturn().getResponse().getContentAsByteArray(), List.class);
			Assert.assertNotNull(projects);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void testFindProjectByProjectCode() {
		try {
			ResultActions resultActions=mockMvc.perform( MockMvcRequestBuilders
				      .get("http://localhost:5379/project/findByStatus").param("status","A")
				      .accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON_VALUE))
				          .andExpect(MockMvcResultMatchers.status().isOk());
			projects=(List<Project>)objectMapper.readValue(resultActions.andReturn().getResponse().getContentAsByteArray(), List.class);
			Assert.assertNotNull(projects);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	@Test
	public void testRemoveProjectByProjectCode() {
		try {
			ResultActions resultActions=mockMvc.perform( MockMvcRequestBuilders
				      .delete(BASE_URL+"/project/deleteByProjectCode").param("projectCode",project.getCode())
				      .accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON_VALUE))
				          .andExpect(MockMvcResultMatchers.status().isOk());
			Integer deleteRecords=objectMapper.readValue(resultActions.andReturn().getResponse().getContentAsByteArray(),Integer.class);
			Assert.assertEquals(1, deleteRecords.intValue());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}
