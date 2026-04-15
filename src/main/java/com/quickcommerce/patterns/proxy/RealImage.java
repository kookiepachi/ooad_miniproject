package com.quickcommerce.patterns.proxy;

public class RealImage implements Image {
    private String imageUrl;
    private long imageSize;

    public RealImage(String imageUrl, long imageSize) {
        this.imageUrl = imageUrl;
        this.imageSize = imageSize;
        loadImage(); // Expensive operation
    }

    private void loadImage() {
        System.out.println("[RealImage] Loading image from: " + imageUrl + 
                          " | Size: " + (imageSize / 1024) + " KB");
    }

    @Override
    public void display() {
        System.out.println("[RealImage] Displaying: " + imageUrl);
    }
}
