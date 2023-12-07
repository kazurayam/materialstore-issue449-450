import java.nio.file.Files
import java.nio.file.Path

import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.openqa.selenium.Keys
import org.openqa.selenium.WebDriver

import com.kazurayam.inspectus.core.Intermediates
import com.kazurayam.materialstore.core.FileType
import com.kazurayam.materialstore.core.JobName
import com.kazurayam.materialstore.core.JobTimestamp
import com.kazurayam.materialstore.core.Material
import com.kazurayam.materialstore.core.MaterialList
import com.kazurayam.materialstore.core.Metadata
import com.kazurayam.materialstore.core.QueryOnMetadata
import com.kazurayam.materialstore.core.SortKeys
import com.kazurayam.materialstore.core.Store
import com.kms.katalon.core.testobject.ConditionType
import com.kms.katalon.core.testobject.TestObject
import com.kms.katalon.core.webui.driver.DriverFactory
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI

/**
 * Test Cases/main/DuckDuckGo/materialize
 *
 */
TestObject makeTestObject(String id, String xpath) {
	TestObject tObj = new TestObject(id)
	tObj.addProperty("xpath", ConditionType.EQUALS, xpath)
	return tObj
}

assert store != null
assert jobName != null
assert jobTimestamp != null

String startingURL = "https://kazurayam.github.io/myApple"

WebUI.openBrowser("")
WebUI.setViewPortSize(1024, 1000)
WebUI.navigateToUrl(startingURL)

// <a>page1</a> element
TestObject toLink = makeTestObject("page1", "//a[text()='page1']")
WebUI.verifyElementPresent(toLink, 10)
WebUI.click(toLink)
TestObject toImg = makeTestObject("img1", "//img[@id='apple']")
WebUI.verifyElementPresent(toImg, 10)
// now take the screenshot and store the PNG file of the Apple image
URL pageURL = new URL(WebUI.getUrl())
Metadata metadata =
	Metadata.builder(pageURL)
			.put("step", "01")
			.put("label", "the original")
			.exclude("URL.port", "URL.protocol")
			.build()
saveElementScreenshot(store, jobName, jobTimestamp, toImg, metadata)


WebUI.closeBrowser()



/**
 *
 * @param metadata
 * @return
 */
def saveElementScreenshot(Store store, JobName jobName,
		JobTimestamp jobTimestamp, TestObject tObj, Metadata metadata) {
	Objects.requireNonNull(store)
	Objects.requireNonNull(jobName)
	Objects.requireNonNull(jobTimestamp)
	Objects.requireNonNull(tObj)
	Objects.requireNonNull(metadata)
	// take a screenshot and save the image into a temporary file using Katalon's built-in keyword
	Path tempFile = Files.createTempFile(null, null);
	WebUI.takeElementScreenshot(tempFile.toAbsolutePath().toFile().toString(), tObj)
	// save the image file into the materialstore
	Material image = store.write(jobName, jobTimestamp, FileType.PNG, metadata, tempFile)
	assert image != null
}


