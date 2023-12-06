import java.nio.file.Files

import org.openqa.selenium.WebDriver

import com.kazurayam.inspectus.materialize.selenium.WebPageMaterializingFunctions
import com.kazurayam.materialstore.core.Material
import com.kms.katalon.core.webui.driver.DriverFactory
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI

Objects.requireNonNull(store)
Objects.requireNonNull(jobName)
Objects.requireNonNull(jobTimestamp)
Objects.requireNonNull(targetList)

WebUI.openBrowser('')
WebUI.setViewPortSize(1024, 800)

WebDriver driver = DriverFactory.getWebDriver()

targetList.eachWithIndex { target, index ->
	
	WebUI.navigateToUrl(target.getUrl().toExternalForm())
	
	// take and store the entire screenshot using AShot
	WebPageMaterializingFunctions pmf = new WebPageMaterializingFunctions(store, jobName, jobTimestamp)
	pmf.setScrollTimeout(1000)
	Map<String, String> attributes = ["step": String.format("%02d", index + 1)]
	
	// save as PNG
	Material png = pmf.storeEntirePageScreenshot.accept(driver, target, attributes)
	assert Files.exists(png.toPath())
	
	// save as JPEG
	Material jpeg = pmf.storeEntirePageScreenshotAsJpeg.accept(driver, target, attributes)
	assert Files.exists(jpeg.toPath())
	
	// take and store the HTML source
	Material html = pmf.storeHTMLSource.accept(driver, target, attributes)
	assert Files.exists(html.toPath())
}

WebUI.closeBrowser()