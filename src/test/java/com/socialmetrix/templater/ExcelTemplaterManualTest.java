package com.socialmetrix.templater;

import java.io.*;
import java.util.*;

import com.socialmetrix.templater.ExcelTemplater;

public class ExcelTemplaterManualTest {

	public static void main(String[] args) throws FileNotFoundException {
		InputStream inputStream = ExcelTemplaterManualTest.class.getResourceAsStream("dashboard.xlsx");
		new ExcelTemplater().renderTemplateTo(inputStream, new Data(), new FileOutputStream("rendered.xlsx"));
		System.out.println("OK");
	}

	public static class Data {
		public Date startDate = new Date(1411586122000L);
		public Date endDate = new Date(1411845322000L);

		public Collection<Date> dates() {
			ArrayList<Date> dates = new ArrayList<Date>();
			for (int i = 0; i < 30; i++) {
				dates.add(new Date(1411700400000L + (86400000 * i)));
			}
			return dates;
		}

		public List<Brand> getBrands() {
			ArrayList<Brand> brands = new ArrayList<Brand>();
			for (int i = 0; i < 5; i++) {
				brands.add(new Brand());
			}
			return brands;
		}
	}

	public static class Brand {
		private String name;
		private Integer newFans;
		private Integer postInteractions;

		public Brand() {
			this.name = randomString(10);
			this.newFans = rnd.nextInt(100) + 5;
			this.postInteractions = rnd.nextInt(100) + 5;
		}

		public Iterable<Metrics> evolution() {
			ArrayList<Metrics> evolution = new ArrayList<Metrics>();
			for (int i = 0; i < 30; i++) {
				evolution.add(new Metrics());
			}
			return evolution;
		}

		private static final String validChars = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
		private static Random rnd = new Random();

		private String randomString(int len) {
			StringBuilder sb = new StringBuilder(len);
			for (int i = 0; i < len; i++)
				sb.append(validChars.charAt(rnd.nextInt(validChars.length())));
			return sb.toString();
		}

		public static class Metrics {
			public int newFans = rnd.nextInt(200) + 5;
			public int postInteractions = rnd.nextInt(200) + 5;
		}

		public String getName() {
			return name;
		}

		public Integer getNewFans() {
			return newFans;
		}

		public Integer getPostInteractions() {
			return postInteractions;
		}
	}

}
