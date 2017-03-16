package com.spectralogic.tpfr.client

import com.spectralogic.tpfr.client.model.Phase

data class ReWrapStatusExpected(val phase: Phase,
                                val percentComplete: String,
                                val errorCode: String?,
                                val errorMessage: String?)