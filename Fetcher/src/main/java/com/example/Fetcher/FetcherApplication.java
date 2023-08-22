package com.example.Fetcher;

import com.example.Fetcher.Controller.MainController;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

@EnableScheduling
@SpringBootApplication
public class FetcherApplication {

	public static void main(String[] args) {
		SpringApplication.run(FetcherApplication.class, args);
	}

	/**
	 * On demand fetcher with Rest API
	 */
	@Scheduled(cron="* * 5-23 * * *")
	void runOnDemandFetcher() { }

	/**
	 * Scheduled update on:
	 * 0 second
	 * 0 minute
	 * 0 AM
	 * not set day of month
	 * not set month
	 * Every day of week
	 */
	@Scheduled(cron = "0 0 1 * * *")
	void runFetcherDailyJob() {
		String updatePeriod = "Daily";
		MainController controller = new MainController(updatePeriod);
		controller.startUpdate();
	}

	/**
	 * Scheduled update on:
	 * 0 second
	 * 0 minute
	 * 4 AM
	 * Not set day of month
	 * Not set month
	 * Saturday
	 */
	@Scheduled(cron = "0 0 4 * * SAT")
	void runFetcherWeeklyJob() {
		String updatePeriod = "Weekly";
		MainController controller = new MainController(updatePeriod);
		controller.startUpdate();
	}

	/**
	 * Scheduled update on:
	 * 0 second
	 * 0 minute
	 * 21
	 * Day 1 of month
	 * Not set Month
	 * Not set weekday
	 */
	@Scheduled(cron = "0 0 23 1 * *")
	void runFetcherMonthlyJob() {
		String updatePeriod = "Monthly";
		MainController controller = new MainController(updatePeriod);
		controller.startUpdate();
	}
}
