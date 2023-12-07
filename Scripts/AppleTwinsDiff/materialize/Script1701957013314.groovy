
import static com.kms.katalon.core.testcase.TestCaseFactory.findTestCase

import java.nio.file.Path
import java.nio.file.Paths

import com.kazurayam.inspectus.materialize.discovery.Sitemap2
import com.kazurayam.inspectus.materialize.discovery.SitemapLoader2
import com.kazurayam.inspectus.materialize.discovery.Target
import com.kazurayam.ks.globalvariable.ExecutionProfilesLoader
import com.kms.katalon.core.configuration.RunConfiguration
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI

import internal.GlobalVariable

/**
 * Test Cases/main/MyAdmin/materialize
 *
 */
// check params which should be passed as the arguments of WebUI.callTestCases() call
Objects.requireNonNull(store)
Objects.requireNonNull(jobName)
Objects.requireNonNull(jobTimestamp)
Objects.requireNonNull(environment)

String executionProfile = environment.toString()

List<Target> targetList = getTargetList(executionProfile)

WebUI.comment("targetList.size()=" + targetList.size())

WebUI.callTestCase(findTestCase("Test Cases/AppleTwinsDiff/processTargetList"),
						[
							"store": store,
							"jobName": jobName,
							"jobTimestamp": jobTimestamp,
							"targetList": targetList
						])

/**
 * look at the Execution Profile to find a CSV file
 * where list of multiple target URLs are written
 */
List<Target> getTargetList(String executionProfile) {
	
	// utility class that loads specified Execution Profiles to make the GlobalVariable.SITEMAP accessible
	ExecutionProfilesLoader profilesLoader = new ExecutionProfilesLoader()
	profilesLoader.loadProfile(executionProfile)
	
	WebUI.comment("GlobalVariable.URL_PREFIX=" + GlobalVariable.URL_PREFIX)
	WebUI.comment("GlobalVariable.SITEMAP=" + GlobalVariable.SITEMAP)
	
	// identify the sitemap file
	Path sitemapFile = Paths.get(RunConfiguration.getProjectDir()).resolve(GlobalVariable.SITEMAP)
	
	// create the variable bindings
	Map<String, String> bindings = new HashMap<>()
	bindings.put("URL_PREFIX", GlobalVariable.URL_PREFIX)
	
	// create an instance of Sitemap
	SitemapLoader2 loader = new SitemapLoader2()
	Sitemap2 sitemap = loader.loadSitemapJson(sitemapFile, bindings)
	
	WebUI.comment("sitemap.size()=" + sitemap.size())
	assert sitemap.size() > 0
	
	// return the list or targets
	return sitemap.getTargetList()
}
