package home.saied.composesamples.ui.search

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import home.saied.samples.Sample
import home.saied.samples.sampleModules

data class SampleWithPath(
    val sample: Sample,
    val moduleIndex: Int,
    val fileIndex: Int,
    val sampleIndex: Int
)

@Stable
interface SearchScreenState {
    var searchString: String?
    val isSearching: Boolean
    val searchResult: List<SampleWithPath>?
}

@Composable
fun rememberSearchState(initialSearchStr: String? = null): SearchScreenState = remember {
    SearchStateImplementation(initialSearchStr)
}

private class SearchStateImplementation(initialSearchStr: String?) : SearchScreenState {
    private data class SearchState(
        override var searchString: String?,
        override val isSearching: Boolean,
        override val searchResult: List<SampleWithPath>?
    ) : SearchScreenState

    private val _searchState = mutableStateOf(
        SearchState(
            initialSearchStr,
            isSearching = initialSearchStr != null,
            searchResult = null
        )
    )

    override var searchString: String?
        get() = _searchState.value.searchString
        set(value) {
            if (value != null)
                performSearch(value)
        }
    override val isSearching: Boolean
        get() = _searchState.value.isSearching
    override val searchResult: List<SampleWithPath>?
        get() = _searchState.value.searchResult

    private fun performSearch(searchString: String) {
        _searchState.value = SearchState(searchString, true, searchResult = null)
        val searchRes = run {
            sampleModules.asSequence()
                .flatMapIndexed { moduleIndex, module ->
                    module.sampleFileList.asSequence().flatMapIndexed { fileIndex, file ->
                        file.sampleList.asSequence().mapIndexed { sampleIndex, sample ->
                            SampleWithPath(sample, moduleIndex, fileIndex, sampleIndex)
                        }
                    }
                }
                .filter {
                    it.sample.name.lowercase().contains(searchString.lowercase())
                }.toList()
        }
        _searchState.value = SearchState(searchString, false, searchResult = searchRes)
    }
}