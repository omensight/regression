package com.alphemsoft.education.regression.dataexporter

sealed class FileData(
    val fileName: String,
    val commonExtension: String
) {
    class Csv(fileName: String = "") : FileData(fileName, Format.CSV.extension)
    class Excel(fileName: String = "") : FileData(fileName, Format.XLSX.extension)
    companion object {
        fun getSupportedFormats(): List<String> {
            return Format.values().map { it.extension }
        }
    }
    enum class Format(val extension: String){
        CSV("csv"),
        XLSX("xlsx")
    }
}