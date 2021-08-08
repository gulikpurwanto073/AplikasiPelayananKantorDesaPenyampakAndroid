package com.sona.ktp.activity.jenis;

import com.google.gson.annotations.SerializedName;

public class SemuajenisItem {

	@SerializedName("jenis")
	private String jenis;

	@SerializedName("id_jenis")
	private String id_jenis;

	@SerializedName("tipe")
	private String tipe;

	public String getJenis() {
		return jenis;
	}

	public void setJenis(String jenis) {
		this.jenis = jenis;
	}

	public String getId_jenis() {
		return id_jenis;
	}

	public void setId_jenis(String id_jenis) {
		this.id_jenis = id_jenis;
	}

	public String getTipe() {
		return tipe;
	}

	public void setTipe(String tipe) {
		this.tipe = tipe;
	}

	@Override
 	public String toString(){
		return 
			"SemuajenisItem{" +
			"jenis = '" + jenis + '\'' +
			",id_jenis = '" + id_jenis + '\'' +
			",tipe = '" + tipe + '\'' +
			"}";
		}
}