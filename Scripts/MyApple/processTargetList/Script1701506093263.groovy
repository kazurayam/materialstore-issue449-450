import java.nio.file.Files

import org.openqa.selenium.By
import org.openqa.selenium.WebDriver

import com.kazurayam.inspectus.materialize.discovery.Target
import com.kazurayam.inspectus.materialize.selenium.WebElementMaterializingFunctions
import com.kazurayam.ks.testobject.TestObjectExtension
import com.kazurayam.materialstore.core.Material
import com.kms.katalon.core.testobject.TestObject
import com.kms.katalon.core.webui.driver.DriverFactory
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI

TestObject toTestObject(Target target) {
	By by = target.getHandle().getBy()
	println "By: " + by
	TestObject tObj = TestObjectExtension.create(by)
	return tObj	
}

Objects.requireNonNull(store)
Objects.requireNonNull(jobName)
Objects.requireNonNull(jobTimestamp)
Objects.requireNonNull(targetList)

WebUI.openBrowser('')
WebUI.setViewPortSize(1024, 800)

WebDriver driver = DriverFactory.getWebDriver()

targetList.eachWithIndex { target, index ->
	println "accessing " + target.getUrl().toExternalForm()
	WebUI.navigateToUrl(target.getUrl().toExternalForm())
	TestObject handle = toTestObject(target)
	// println "target=" + target.toString()
	// println "handle=" + handle.toString()
	WebUI.verifyElementPresent(handle, 10)
	
	// take Element screenshot
	WebElementMaterializingFunctions emf = new WebElementMaterializingFunctions(store, jobName, jobTimestamp)
	emf.setScrollTimeout(1000)
	Map<String, String> attributes = ["step": String.format("%02d", index + 1)]
	
	// save as PNG
	Material png = emf.storeElementScreenshot.accept(driver, target, attributes, target.getHandle().getBy())
	assert Files.exists(png.toPath())
}

WebUI.closeBrowser()
