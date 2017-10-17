package se.datasektionen.calypso.util;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
public class StringUtilsTest {

	@Test
	public void facebookEvent() throws Exception {
		Assert.assertEquals("420704381659526", "https://www.facebook.com/events/420704381659526/?acontext=%7B%22ref%22%3A%2225%22%2C%22sid_reminder%22%3A%226468303224191730954%22%2C%22action_history%22%3A%22null%22%7D");
		Assert.assertEquals("420704381659526", "https://www.facebook.com/events/420704381659526/");
		Assert.assertEquals("116734949016560", "https://m.facebook.com/events/116734949016560/?acontext=%7B%22ref%22%3A%222%22%2C%22ref_dashboard_filter%22%3A%22upcoming%22%2C%22action_history%22%3A%22null%22%7D&ref=bookmarks");
		Assert.assertEquals("116734949016560", "https://m.facebook.com/events/116734949016560");
	}

}