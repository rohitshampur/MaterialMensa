package de.prttstft.materialmensa.services;

import de.prttstft.materialmensa.logging.L;
import me.tatarka.support.job.JobParameters;
import me.tatarka.support.job.JobService;

public class MyService extends JobService {

    @Override
    public boolean onStartJob(JobParameters jobParameters) {
        L.t(this, "onStartJob");
        jobFinished(jobParameters, false);
        return false;
    }

    @Override
    public boolean onStopJob(JobParameters jobParameters) {
        L.t(this, "onStopJob");
        return false;
    }
}
