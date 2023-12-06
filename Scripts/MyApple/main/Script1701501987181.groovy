import java.nio.file.Path
import java.nio.file.Paths

import com.kazurayam.inspectus.core.Environment
import com.kazurayam.inspectus.core.Inspectus
import com.kazurayam.inspectus.core.Intermediates
import com.kazurayam.inspectus.core.Parameters
import com.kazurayam.inspectus.katalon.KatalonTwinsDiff
import com.kazurayam.materialstore.core.metadata.IgnoreMetadataKeys
import com.kazurayam.materialstore.core.JobName
import com.kazurayam.materialstore.core.JobTimestamp
import com.kazurayam.materialstore.core.SortKeys
import com.kazurayam.materialstore.core.Store
import com.kazurayam.materialstore.core.Stores
import com.kms.katalon.core.configuration.RunConfiguration
import com.kms.katalon.core.util.KeywordUtil

/**
 * Test Cases/MyAdmin/main
 *
 */
Path projectDir = Paths.get(RunConfiguration.getProjectDir())
Path local = projectDir.resolve("store")
Path remote = projectDir.resolve("store-backup")
Store store = Stores.newInstance(local)
Store backup = Stores.newInstance(remote)
JobName jobName = new JobName("MyApple")
JobTimestamp jobTimestamp = JobTimestamp.now()
SortKeys sortKeys = new SortKeys("step")

Parameters p =
	new Parameters.Builder()
		.store(store)
		.backup(backup)
		.jobName(jobName)
		.jobTimestamp(jobTimestamp)
		.ignoreMetadataKeys(new IgnoreMetadataKeys.Builder()
			.ignoreKeys("URL.path", "URL.port", "URL.protocol", "image-width", "image-height")
			.build())
		.sortKeys(sortKeys)
		.threshold(3.0)    // ignore differences less than 3.0%
		.build();

Inspectus inspectus =
	new KatalonTwinsDiff("Test Cases/MyApple/materialize",
						new Environment("MyApple_ProductionEnv"),
						new Environment("MyApple_DevelopmentEnv"))
Intermediates result = inspectus.execute(p)

if (result.getWarnings() > 0) {
	//KeywordUtil.markFailed("there found ${result.getWarnings()} warning(s)");
	KeywordUtil.markWarning("there found ${result.getWarnings()} warning(s)");
}

