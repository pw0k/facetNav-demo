package com.pw.facetnav.exception;

public class AdNotFoundException extends RuntimeException {
    public AdNotFoundException(Long adId) {
        super("Ad not found with id: " + adId);
    }
}
