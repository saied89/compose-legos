package home.saied.composesamples.ui

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import home.saied.samples.Sample
import home.saied.samples.sampleModules

data class SampleWithPath(
    val sample: Sample,
    val moduleIndex: Int,
    val fileIndex: Int,
    val sampleIndex: Int
)

class HomeViewModel : ViewModel() {

    sealed class HomeState {
        abstract val searchString: String?

        object MODULES : HomeState() {
            override var searchString: String? = null
        }

        data class SearchState(
            override val searchString: String?,
            val isSearching: Boolean,
            val searchResult: List<SampleWithPath>?
        ) : HomeState()
    }


    private val _homeState = mutableStateOf<HomeState>(
        HomeState.MODULES
    )
    val homeState: State<HomeState> = _homeState

    var searchStr: String?
        get() = _homeState.value.searchString
        set(value) {
            if (value == null)
                _homeState.value = HomeState.MODULES
            else
                performSearch(value)
        }

    private fun performSearch(searchString: String) {
        _homeState.value = HomeState.SearchState(searchString, true, searchResult = null)
        val searchRes = run {
            if (searchString.isNotBlank())
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
            else
                emptyList()
        }
        _homeState.value = HomeState.SearchState(searchString, false, searchResult = searchRes)
    }
}