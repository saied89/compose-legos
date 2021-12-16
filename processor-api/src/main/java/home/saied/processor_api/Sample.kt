package home.saied.processor_api

data class SampleFile(val fileName: String, val sampleList: List<Sample>)

data class Sample(val name: String, val body: String, val docStr: String)