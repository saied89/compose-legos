package home.saied.composesamples.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.viewModelFactory
import home.saied.samples.Sample
import home.saied.samples.SampleModule
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

data class SampleWithPath(
    val sample: Sample,
    val moduleIndex: Int,
    val fileIndex: Int,
    val sampleIndex: Int
)

class HomeViewModel(
    private val sampleModuleList: List<SampleModule>,
    private val defaultDispatcher: CoroutineDispatcher = Dispatchers.Default
    ) : ViewModel() {

    data class HomeState(
        val searchStr: String = "",
        val searchResult: List<SampleWithPath> = emptyList()
    )


    private val _homeState = MutableStateFlow(HomeState())
    val homeState: StateFlow<HomeState> = _homeState.asStateFlow()

    fun setSearchStr(searchStr: String) {
        _homeState.value = _homeState.value.copy(searchStr = searchStr, searchResult = emptyList())
        viewModelScope.launch {
            _homeState.update {
                val searchResult = findSamples(it.searchStr)
                it.copy(searchResult = searchResult)
            }
        }
    }

    // TODO maybe move this functionality to repository
    private suspend fun findSamples(searchString: String): List<SampleWithPath> =
        withContext(defaultDispatcher) {
            if (searchString.isNotBlank())
                sampleModuleList.asSequence()
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

    companion object {
        fun factory(sampleModuleList: List<SampleModule>) = viewModelFactory {
            addInitializer(HomeViewModel::class) {
                HomeViewModel(sampleModuleList)
            }
        }
    }
}