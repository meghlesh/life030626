package com.cws.cwslife.model;

public class AdminDashboard {
	
	private long mediaUploads;
	
	public AdminDashboard(long mediaUploads) {
        this.mediaUploads = mediaUploads;
    }

	public long getMediaUploads() {
		return mediaUploads;
	}

	public void setMediaUploads(long mediaUploads) {
		this.mediaUploads = mediaUploads;
	}
}
