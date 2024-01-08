package com.pw.facetnav.service;

import com.pw.facetnav.exception.AdNotFoundException;
import com.pw.facetnav.model.ad.Ad;
import com.pw.facetnav.model.ad.AdRepository;
import com.pw.facetnav.model.ad.AdSpecifications;
import com.pw.facetnav.model.attribute.AttributeFilterRecord;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class AdService {

    private final AdRepository adRepository;

    @Transactional(readOnly = true)
    public List<Ad> findAdsByAttributesAndCategory(String categoryName, Set<AttributeFilterRecord> attributeFilters) {
        Specification<Ad> spec = AdSpecifications.withAttributesAndCategory(categoryName, attributeFilters);
        return adRepository.findAll(spec);
    }

    @Transactional(readOnly = true)
    public List<Ad> getAllAds() {
        return adRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Ad getAdById(Long id) {
        return adRepository.findById(id).orElseThrow(() -> new AdNotFoundException(id));
    }

    @Transactional
    public Ad saveAd(Ad ad) {
        return adRepository.save(ad);
    }

    @Transactional
    public Ad updateAd(Long id, Ad updatedAd) {
        Ad existingAd = adRepository.findById(updatedAd.getId()).orElseThrow(() -> new AdNotFoundException(id));

        existingAd.setTitle(updatedAd.getTitle());
        existingAd.setDescription(updatedAd.getDescription());
        existingAd.setCategory(updatedAd.getCategory());

        return adRepository.save(existingAd);
    }

    @Transactional
    public void deleteAd(Long id) {
        if (!adRepository.existsById(id)) {
            throw new AdNotFoundException(id);
        }
        adRepository.deleteById(id);
    }
}

