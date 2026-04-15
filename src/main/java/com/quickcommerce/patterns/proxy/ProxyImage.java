package com.quickcommerce.patterns.proxy;

/**
 * Proxy class that delays loading of RealImage until display() is called
 * This implements lazy loading for high-resolution product images
 */
public class ProxyImage implements Image {
    private String imageUrl;
    private long imageSize;
    private RealImage realImage;

    public ProxyImage(String imageUrl, long imageSize) {
        this.imageUrl = imageUrl;
        this.imageSize = imageSize;
        this.realImage = null;
    }

    @Override
    public void display() {
        if (realImage == null) {
            System.out.println("[ProxyImage] First access - Loading high resolution image...");
            realImage = new RealImage(imageUrl, imageSize);
        }
        realImage.display();
    }

    public void getThumbnail() {
        System.out.println("[ProxyImage] Displaying thumbnail for: " + imageUrl);
    }
}
