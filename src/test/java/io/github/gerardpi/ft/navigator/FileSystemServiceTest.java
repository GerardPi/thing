package io.github.gerardpi.ft.navigator;

import com.tngtech.jgiven.Stage;
import com.tngtech.jgiven.annotation.Format;
import com.tngtech.jgiven.annotation.Quoted;
import com.tngtech.jgiven.junit5.SimpleScenarioTest;
import io.github.gerardpi.EntryListFormatter;
import io.github.gerardpi.TestFunctions;
import io.github.gerardpi.ft.Result;
import org.junit.jupiter.api.Test;

import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

class FileSystemServiceTest extends SimpleScenarioTest<FileSystemServiceTest.State> {
    static {
        // Make sure jgiven writes reports in target instead of project root
        System.setProperty("jgiven.report.dir", "target/jgiven-reports");
    }

    @Test
    void test_reading_one_file_entry() {
        given().the_$_file_system("default");
        when().reading_file_$("src/test/java/io/github/gerardpi/ft/navigator/FileSystemServiceTest.java");
        then().the_entry_ready_is_a_file_$_with_size_at_least_bytes_$_that_$_hidden_and_has_profile_$("FileSystemServiceTest.java", 1600L, "is not", "-rw-");
    }

    @Test
    void test_reading_a_directory() {
        given().the_$_file_system("default");
        when().reading_directory_$("");
        then().reading_entries_$_successfull_and_results_in_$_entries("is", 7)
                .and()
                .the_entries_were_found_in_a_path_that_starts_with_$_and_ends_with_$("/home", "/file-thing")
                .and().the_directory_entries_found_are_$(List.of(".git", ".idea", "src", "target"))
                .and().the_file_entries_found_are_$(List.of(".gitignore", "pom.xml","README.adoc"));
    }

    @Test
    void test_reading_a_non_existent_file() {
        when().reading_file_$("/tmp/file_does_not_exist");
        then().that_entry_has_a_problem_$_for_operation_$_on_$("ENTRY_NOT_FOUND", "GET_ATTRIBUTES", "/tmp/file_does_not_exist")
        .and().the_full_problem_reads_$("Performing GET_ATTRIBUTES on '/tmp/file_does_not_exist' resulted in ENTRY_NOT_FOUND. Entry requested could not be found: /tmp/file_does_not_exist");
    }

    static class State extends Stage<State> {
        private FileSystem fileSystem;
        private FileSystemService sut;
        private Entry entry;
        private Result<Entries, NavigatorException> fileSystemResult;

        State reading_file_$(@Quoted String givenFileName) {
            this.sut = new FileSystemService(this.fileSystem);
            this.entry = sut.readEntry(Paths.get(givenFileName));
            return self();
        }

        State reading_directory_$(@Quoted String givenDirectoryName) {
            this.sut = new FileSystemService(this.fileSystem);
            this.fileSystemResult = sut.readEntries(Paths.get(givenDirectoryName));
            return self();
        }

        State the_$_file_system(@Quoted String givenFilesystemName) {
            this.fileSystem = getFileSystem(givenFilesystemName);
            return self();
        }

        private FileSystem getFileSystem(String fileSystemName) {
            return switch (fileSystemName) {
                case "default" -> FileSystems.getDefault();
                default -> throw new IllegalArgumentException("Don't know filesystem '" + fileSystemName + "'");
            };
        }

        State the_entry_ready_is_a_file_$_with_size_at_least_bytes_$_that_$_hidden_and_has_profile_$(
                @Quoted String expectedFilename,
                @Quoted long expectedMinimalSize,
                @Quoted String isOrIsNotHidden,
                @Quoted String expectedProfile) {
            assertThat(this.entry.hasProblem()).isFalse();
            assertThat(entry.getName()).isEqualTo(expectedFilename);
            assertThat(entry.getSize()).isGreaterThan(expectedMinimalSize);
            assertThat(entry.getProfile()).isEqualTo(expectedProfile);
            assertThat(entry.isHidden()).isEqualTo(TestFunctions.isOrIsNot(isOrIsNotHidden));
            return self();
        }

        State the_entries_were_found_in_directory_$(@Quoted String expectedDirectory) {
            Entries entries = this.fileSystemResult.successValue();
            assertThat(entries.getContext().getCurrentDirectory()).isEqualTo(expectedDirectory);
            return self();
        }

        State the_entries_were_found_in_a_path_that_starts_with_$_and_ends_with_$(@Quoted String expectedPathPrefix, @Quoted String expectedPathSuffix) {
            Entries entries = this.fileSystemResult.successValue();
            assertThat(entries.getContext().getCurrentDirectory()).startsWith(expectedPathPrefix);
            assertThat(entries.getContext().getCurrentDirectory()).endsWith(expectedPathSuffix);
            return self();
        }

        State reading_entries_$_successfull_and_results_in_$_entries(
                @Quoted String expectedIsOrIsNotSuccessful,
                @Quoted int expectedEntryCount) {
            Entries entries = this.fileSystemResult.successValue();
            assertThat(this.fileSystemResult.successful()).isEqualTo(TestFunctions.isOrIsNot(expectedIsOrIsNotSuccessful));
            assertThat(entries.getEntryList()).hasSize(expectedEntryCount);
            return self();
        }

        State the_directory_entries_found_are_$(@Format(value = EntryListFormatter.class) List<String> expectedDirEntryNames) {
            Entries entries = this.fileSystemResult.successValue();
            List<String> actualNames = entries.getDirEntries().stream().map(Entry::getName).collect(Collectors.toList());
            assertThat(actualNames).containsAll(expectedDirEntryNames);
            return self();
        }

        State the_file_entries_found_are_$(@Format(value = EntryListFormatter.class) List<String> expectedFileEntryNames) {
            Entries entries = this.fileSystemResult.successValue();
            List<String> actualNames = entries.getFileEntries().stream().map(Entry::getName).collect(Collectors.toList());
            assertThat(actualNames).containsAll(expectedFileEntryNames);
            return self();
        }
        State that_entry_has_a_problem_$_for_operation_$_on_$(@Quoted String expectedProblemType, @Quoted String expectedOperation, @Quoted String expectedSubject) {
            assertThat(this.entry.hasProblem()).isTrue();
            NavigatorException navigatorException = this.entry.getNavigatorException().get();
            assertThat(navigatorException.getOperation()).isEqualTo(NavigatorException.Operation.valueOf(expectedOperation));
            assertThat(navigatorException.getType()).isEqualTo(NavigatorException.Type.valueOf(expectedProblemType));
            assertThat(navigatorException.getSubject()).isEqualTo(expectedSubject);
            return self();
        }

        State the_full_problem_reads_$(@Quoted String expectedFullProblem) {
            assertThat(this.entry.hasProblem()).isTrue();
            NavigatorException navigatorException = this.entry.getNavigatorException().get();
            assertThat(navigatorException.getMessage()).isEqualTo(expectedFullProblem);
            return self();
        }
    }
}
