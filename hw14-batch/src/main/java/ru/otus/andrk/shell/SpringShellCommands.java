package ru.otus.andrk.shell;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.configuration.JobRegistry;
import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;

import static ru.otus.andrk.job.JobConstants.CLEAR_PARAMETER_NAME;
import static ru.otus.andrk.job.JobConstants.IMPORT_LIBRARY_JOB_NAME;

@RequiredArgsConstructor
@ShellComponent
public class SpringShellCommands {

    private final JobLauncher jobLauncher;

    private final JobExplorer jobExplorer;

    private final JobRegistry jobRegistry;


    @ShellMethod(value = "start migration with clear", key = "sm")
    @SuppressWarnings("unused")
    public void startMigrationJobWithClear() throws Exception {
        startImportLibraryJob(true);
    }

    @ShellMethod(value = "start migration without clear", key = "sm-no")
    @SuppressWarnings("unused")
    public void startMigrationJobWithoutClear() throws Exception {
        startImportLibraryJob(false);
    }


    @ShellMethod(value = "show info", key = "i")
    @SuppressWarnings("unused")
    public void showInfo() {
        System.out.println(jobExplorer.getJobNames());
        System.out.println(jobExplorer.getLastJobInstance(IMPORT_LIBRARY_JOB_NAME));
    }

    @ShellMethod(value = "Exit application", key = "quit")
    @SuppressWarnings("unused")
    public void shutDown() {
        System.exit(0);
    }

    private void startImportLibraryJob(boolean clear) throws Exception {
        Job job = jobRegistry.getJob(IMPORT_LIBRARY_JOB_NAME);
        var jobParameters = new JobParametersBuilder(jobExplorer)
                .getNextJobParameters(job)
                .addJobParameter(CLEAR_PARAMETER_NAME, clear, Boolean.class)
                .toJobParameters();
        var jobExecution = jobLauncher.run(job, jobParameters);
        System.out.println(jobExecution);
    }

}
