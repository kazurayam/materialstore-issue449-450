import static com.kms.katalon.core.testcase.TestCaseFactory.findTestCase

import java.nio.file.Path
import java.nio.file.Paths

import com.kazurayam.inspectus.core.Parameters
import com.kazurayam.materialstore.core.JobName
import com.kazurayam.materialstore.core.JobTimestamp
import com.kazurayam.materialstore.core.SortKeys
import com.kazurayam.materialstore.core.Store
import com.kazurayam.materialstore.core.Stores
import com.kms.katalon.core.configuration.RunConfiguration
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI


/**
 * Test Cases/MyAdmin/run_materialize
 * 
 */

Path projectDir = Paths.get(RunConfiguration.getProjectDir())
Store store = Stores.newInstance(projectDir.resolve("store"))
JobName jobName = new JobName("MyAdmin_run_materialize")
JobTimestamp jobTimestamp = JobTimestamp.now()
SortKeys sortKeys = new SortKeys("step", "URL.host", "URL.path")

Map<String, Object> p = new LinkedHashMap<>()
p.put(Parameters.KEY_store, store)
p.put(Parameters.KEY_jobName, jobName)
p.put(Parameters.KEY_jobTimestamp, jobTimestamp)
p.put(Parameters.KEY_sortKeys, sortKeys)

p.put(Parameters.KEY_environment, "MyAdmin_DevelopmentEnv")

// take screenshot PNG and save HTML source
WebUI.callTestCase(findTestCase("Test Cases/MyAdmin/materialize"), p);



