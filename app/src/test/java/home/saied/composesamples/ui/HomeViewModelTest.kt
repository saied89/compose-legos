package home.saied.composesamples.ui

import home.saied.composesamples.MainDispatcherRule
import home.saied.samples.Sample
import home.saied.samples.SampleFile
import home.saied.samples.SampleModule
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Rule
import org.junit.Test

class HomeViewModelTest {

    @get:Rule
    val dispatcherRule = MainDispatcherRule()

    @Test
    fun `WHEN init THEN searchStr and searchResult are empty`() {
        val subject = HomeViewModel(emptyList())

        assert(subject.homeState.value.searchStr.isEmpty())
        assert(subject.homeState.value.searchResult.isEmpty())
    }

    @Test
    fun `WHEN search THEN searchStr and searchResult are correct`() = runTest(dispatcherRule.testDispatcher) {
        val subject = HomeViewModel(mockSampleModuleList, dispatcherRule.testDispatcher)
        subject.setSearchStr("sample")
        assertEquals("sample", subject.homeState.value.searchStr)
        assertEquals(1000, subject.homeState.value.searchResult.size)
    }
}

val mockSampleModuleList = List(10) { moduleIndex ->
    SampleModule(
        "module$moduleIndex",
        "package",
        List(10) {fileIndex ->
            SampleFile(
                "file$moduleIndex$fileIndex",
                "",
                List(10) {sampleIndex ->
                    Sample("sample$moduleIndex$fileIndex$sampleIndex","","")
                }
            )
        }
    )
}