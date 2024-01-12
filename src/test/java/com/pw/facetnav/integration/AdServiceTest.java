package com.pw.facetnav.integration;

import com.pw.facetnav.model.ad.Ad;
import com.pw.facetnav.model.ad.AdRepository;
import com.pw.facetnav.model.attribute.Attribute;
import com.pw.facetnav.model.attribute.AttributeFilterRecord;
import com.pw.facetnav.model.attribute.AttributeRepository;
import com.pw.facetnav.model.category.Category;
import com.pw.facetnav.model.category.CategoryRepository;
import com.pw.facetnav.service.AdService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static junit.framework.TestCase.assertEquals;
import static org.junit.Assert.assertNotNull;


@SpringBootTest
@AutoConfigureMockMvc
@Testcontainers
public class AdServiceTest {

    @Autowired
    private AdRepository adRepository;
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private AttributeRepository attributeRepository;
    @Autowired
    private AdService adService;

    @Container
    private static final PostgreSQLContainer<?> postgresContainer = new PostgreSQLContainer<>("postgres:16.1-alpine");

    static {
        postgresContainer.start();
        System.setProperty("spring.datasource.url", postgresContainer.getJdbcUrl());
        System.setProperty("spring.datasource.username", postgresContainer.getUsername());
        System.setProperty("spring.datasource.password", postgresContainer.getPassword());
    }

    @Test
    public void testFindAdsByAttributesAndCategory() {
        // Given
        Category category = Category.builder().name("Electronics").build();
        categoryRepository.save(category);

        Set<Attribute> attributes = IntStream.rangeClosed(1, 3)
                .mapToObj(i -> Attribute.builder().name("attr" + i).value(i).build())
                .collect(Collectors.toSet());
        attributes.forEach(attributeRepository::save);

        Set<AttributeFilterRecord> attributeFilters = Set.of(
                new AttributeFilterRecord("attr1", 1, "="),
                new AttributeFilterRecord("attr3", 2, ">")
        );

        adRepository.save(Ad.builder().title("Ad1").description("description").category(category).attributes(attributes).build());
        adRepository.save(Ad.builder().title("Ad2").description("description").category(category).attributes(attributes).build());

        // When
        List<Ad> ads = adService.findAdsByAttributesAndCategory("Electronics", attributeFilters);

        // Then
        assertNotNull(ads);
        assertEquals(2, ads.size());
        assertEquals("Ad1", ads.get(0).getTitle());
        assertEquals("Ad2", ads.get(1).getTitle());
    }


}
