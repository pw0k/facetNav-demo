package com.pw.facetnav.in.web;

import com.pw.facetnav.model.ad.Ad;
import com.pw.facetnav.service.AdService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/ads")
@RequiredArgsConstructor
public class AdController {

    private final AdService adService;

    @GetMapping
    public List<Ad> getAllAds() {
        return adService.getAllAds();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Ad> getAdById(@PathVariable Long id) {
        return new ResponseEntity<>(adService.getAdById(id), HttpStatus.OK);
    }

    @PostMapping("/create")
    public ResponseEntity<Ad> saveAd(@RequestBody Ad ad) {
        return new ResponseEntity<>(adService.saveAd(ad), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Ad> updateAd(@PathVariable Long id, @RequestBody Ad updatedAd) {
        return new ResponseEntity<>(adService.updateAd(id, updatedAd), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAd(@PathVariable Long id) {
        adService.deleteAd(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
