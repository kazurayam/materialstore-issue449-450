import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

import org.openqa.selenium.Keys
import org.openqa.selenium.WebDriver

import com.kazurayam.ks.globalvariable.ExecutionProfilesLoader

import com.kazurayam.inspectus.materialize.selenium.WebPageMaterializingFunctions
import com.kazurayam.inspectus.materialize.discovery.Target
import com.kazurayam.materialstore.core.Material
import com.kms.katalon.core.webui.driver.DriverFactory
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI

import internal.GlobalVariable

void takeScreenshot(WebDriver driver, URL url, Map<String,String> attributes, WebPageMaterializingFunctions pmf) {
	Target target = Target.builder(url).putAll(attributes).build()
	Material screenshot = pmf.storeEntirePageScreenshotAsJpeg.accept(driver, target, [:])
	Material html = pmf.storeHTMLSource.accept(driver, target, [:])
}

/**
 * Test Cases/main/CURA/materialize
 */
String profile = "CURA_DevelopmentEnv"
ExecutionProfilesLoader profilesLoader = new ExecutionProfilesLoader()
profilesLoader.loadProfile(profile)
Objects.requireNonNull(GlobalVariable.URL)
Objects.requireNonNull(GlobalVariable.Username)
Objects.requireNonNull(GlobalVariable.Password)
Objects.requireNonNull(store)
Objects.requireNonNull(jobName)
Objects.requireNonNull(jobTimestamp)

// -------- setup -----------------------------------------------------
WebUI.openBrowser('')
WebUI.setViewPortSize(1024, 800)
WebUI.navigateToUrl(GlobalVariable.URL)
WebDriver driver = DriverFactory.getWebDriver()

WebPageMaterializingFunctions pmf = new WebPageMaterializingFunctions(store, jobName, jobTimestamp)
pmf.setScrollTimeout(1000)

// -------- The top page is supposed to be open --------------------------------------
WebUI.verifyElementPresent(findTestObject('CURA/Page_CURA Healthcare Service/top/a_Make Appointment'), 15)

// take the screenshot and the page source, save them into the store; using the Katalon keyword
takeScreenshot(driver, new URL(driver.getCurrentUrl()), ["step": "01", "profile": "ProductionEnv"], pmf)
	
// we navigate to the next page (login)
WebUI.click(findTestObject('CURA/Page_CURA Healthcare Service/top/a_Make Appointment'))

// -------- The login page is supposed to be open ------------------------------------
WebUI.verifyElementPresent(findTestObject('CURA/Page_CURA Healthcare Service/login/button_Login'), 15)

// type the Username
assert GlobalVariable.Username != null, "GlobalVariable.Username is not defined"
WebUI.setText(findTestObject('CURA/Page_CURA Healthcare Service/login/input_Username_username'),
	GlobalVariable.Username)

// type the Password
assert GlobalVariable.Password != null, "GlobalVariable.Password is not defined"
WebUI.setText(findTestObject('CURA/Page_CURA Healthcare Service/login/input_Password_password'),
	GlobalVariable.Password)

// we navigate to the next page (appointment)
WebUI.click(findTestObject('CURA/Page_CURA Healthcare Service/login/button_Login'))



// -------- The appointment page is supposed to be open ------------------------------
WebUI.verifyElementPresent(findTestObject('CURA/Page_CURA Healthcare Service/appointment/button_Book Appointment'), 15)

WebUI.selectOptionByValue(findTestObject('CURA/Page_CURA Healthcare Service/appointment/select_Tokyo CURA Healthcare Center'),
	'Hongkong CURA Healthcare Center', true)
WebUI.click(findTestObject('CURA/Page_CURA Healthcare Service/appointment/input_Apply for hospital readmission'))
WebUI.click(findTestObject('CURA/Page_CURA Healthcare Service/appointment/input_Medicaid_programs'))

// set the same day in the next week
def visitDate = LocalDateTime.now().plusWeeks(1)
def visitDateStr = DateTimeFormatter.ofPattern("dd/MM/yyyy").format(visitDate)
WebUI.verifyElementPresent(findTestObject('CURA/Page_CURA Healthcare Service/appointment/input_visit_date'), 15)
WebUI.setText(findTestObject('CURA/Page_CURA Healthcare Service/appointment/input_visit_date'), visitDateStr)
WebUI.sendKeys(findTestObject('CURA/Page_CURA Healthcare Service/appointment/input_visit_date'), Keys.chord(Keys.ENTER))
WebUI.setText(findTestObject('CURA/Page_CURA Healthcare Service/appointment/textarea_Comment_comment'), 'this is a comment')

// take the screenshot and the page source, save them into the store
takeScreenshot(driver, new URL(driver.getCurrentUrl()), ["step": "02", "profile": "ProductionEnv"], pmf)

// we navigate to the next page (summpary)
WebUI.click(findTestObject('CURA/Page_CURA Healthcare Service/appointment/button_Book Appointment'))



// -------- the summary page is supposed to be open ----------------------------------
WebUI.verifyElementPresent(findTestObject('CURA/Page_CURA Healthcare Service/summary/a_Go to Homepage'), 15)

// take the screenshot and the page source, save them into the store
takeScreenshot(driver, new URL(driver.getCurrentUrl()), ["step": "03", "profile": "ProductionEnv"], pmf)

// we navigate to the home page
WebUI.click(findTestObject('CURA/Page_CURA Healthcare Service/summary/a_Go to Homepage'))


// --------------------------------------------------------------------
// we are done
WebUI.closeBrowser()


