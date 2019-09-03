package com.dongtronic.keeptalking

enum class ManualVersion private constructor(val manualName: String, val pages: Int, val introPages: Int) {
    CODE241V1("241 version 1", 23, 4),
    MANUAL("Other", 0, 0);


    companion object {

        fun fromName(name: String): ManualVersion? {
            for (version in ManualVersion.values()) {
                if (version.manualName.equals(name, ignoreCase = true)) {
                    return version
                }
            }
            return null
        }
    }
}
