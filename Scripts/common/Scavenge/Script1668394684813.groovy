import java.nio.file.Path
import java.nio.file.Paths

import com.kazurayam.materialstore.base.manage.StoreCleaner
import com.kazurayam.materialstore.core.JobName
import com.kazurayam.materialstore.core.JobTimestamp
import com.kazurayam.materialstore.core.Store
import com.kazurayam.materialstore.core.Stores
import com.kms.katalon.core.configuration.RunConfiguration

/**
 * Test Cases/common/Scavenge
 *
 * cleans up old artifacts of all JobNames in the remote store.
 *
 * we will retain artifacts within 10 days; clean up the older than 5 days.
 */

Path projectDir = Paths.get(RunConfiguration.getProjectDir())
Path remote = projectDir.resolve("store-backup")
Store backup = Stores.newInstance(remote)
StoreCleaner cleaner = StoreCleaner.newInstance(backup)

JobTimestamp olderThan = JobTimestamp.now().minusDays(7)

List<JobName> jobNames = backup.findAllJobNames()
for (JobName jobName : jobNames) {
	cleaner.cleanup(jobName, olderThan)
}
