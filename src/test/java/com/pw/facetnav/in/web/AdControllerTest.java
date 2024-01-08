package com.pw.facetnav.in.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pw.facetnav.exception.AdNotFoundException;
import com.pw.facetnav.model.ad.Ad;
import com.pw.facetnav.service.AdService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AdController.class)
class AdControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AdService adService;

    @Test
    void getAllAds_ShouldReturnListOfAds() throws Exception {
        List<Ad> ads = List.of(new Ad(), new Ad());
        when(adService.getAllAds()).thenReturn(ads);

        mockMvc.perform(MockMvcRequestBuilders.get("/ads"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(2));

        verify(adService, times(1)).getAllAds();
    }

    @Test
    void getAdById_ExistingId_ShouldReturnAd() throws Exception {
        Long adId = 1L;
        Ad mockAd = Ad.builder().id(adId).build();
        when(adService.getAdById(adId)).thenReturn(mockAd);

        mockMvc.perform(MockMvcRequestBuilders.get("/ads/{id}", adId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L));

        verify(adService, times(1)).getAdById(adId);
    }

    @Test
    void getAdById_NonexistentId_ShouldReturnNotFound() throws Exception {
        Long nonExistentAdId = 2L;
        when(adService.getAdById(nonExistentAdId)).thenThrow(new AdNotFoundException(nonExistentAdId));

        mockMvc.perform(MockMvcRequestBuilders.get("/ads/{id}", nonExistentAdId))
                .andExpect(status().isNotFound());

        verify(adService, times(1)).getAdById(nonExistentAdId);
    }

    @Test
    void createAd_ValidAd_ShouldReturnCreated() throws Exception {
        Ad createdAd = Ad.builder().id(1L).build();
        when(adService.saveAd(any(Ad.class))).thenReturn(createdAd);

        mockMvc.perform(MockMvcRequestBuilders.post("/ads/create")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L));

        verify(adService, times(1)).saveAd(any(Ad.class));
    }

    @Test
    void updateAd_ValidAd_ShouldReturnOk() throws Exception {
        Long adId = 1L;
        Ad updatedAd = Ad.builder().id(adId).build();
        when(adService.updateAd(eq(adId), any(Ad.class))).thenReturn(updatedAd);

        mockMvc.perform(MockMvcRequestBuilders.put("/ads/{id}", adId)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L));

        verify(adService, times(1)).updateAd(eq(adId), any(Ad.class));
    }

    @Test
    void updateAd_NonexistentId_ShouldReturnNotFound() throws Exception {
        Long nonExistentAdId = 1L;
        doThrow(new AdNotFoundException(nonExistentAdId)).when(adService).updateAd(eq(nonExistentAdId), any(Ad.class));

        mockMvc.perform(MockMvcRequestBuilders.put("/ads/{id}", nonExistentAdId)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{}"))
                .andExpect(status().isNotFound());

        verify(adService, times(1)).updateAd(eq(nonExistentAdId), any(Ad.class));
    }

    @Test
    void deleteAd_ExistingId_ShouldReturnNoContent() throws Exception {
        Long adId = 1L;

        mockMvc.perform(MockMvcRequestBuilders.delete("/ads/{id}", adId))
                .andExpect(status().isNoContent());

        verify(adService, times(1)).deleteAd(adId);
    }

    @Test
    void deleteAd_NonexistentId_ShouldReturnNotFound() throws Exception {
        Long nonExistentAdId = 2L;
        doThrow(new AdNotFoundException(nonExistentAdId)).when(adService).deleteAd(nonExistentAdId);

        mockMvc.perform(MockMvcRequestBuilders.delete("/ads/{id}", nonExistentAdId))
                .andExpect(status().isNotFound());

        verify(adService, times(1)).deleteAd(nonExistentAdId);
    }
}