//
//  CountdownStepUi.kt
//
//
//  Copyright © 2022 Sage Bionetworks. All rights reserved.
//
// Redistribution and use in source and binary forms, with or without modification,
// are permitted provided that the following conditions are met:
//
// 1.  Redistributions of source code must retain the above copyright notice, this
// list of conditions and the following disclaimer.
//
// 2.  Redistributions in binary form must reproduce the above copyright notice,
// this list of conditions and the following disclaimer in the documentation and/or
// other materials provided with the distribution.
//
// 3.  Neither the name of the copyright holder(s) nor the names of any contributors
// may be used to endorse or promote products derived from this software without
// specific prior written permission. No license is granted to the trademarks of
// the copyright holders even if such marks are included in this software.
//
// THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
// AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
// IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
// ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE
// FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
// DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
// SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
// CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
// OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
// OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
//

package org.sagebionetworks.motorcontrol.presentation.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.sagebionetworks.assessmentmodel.presentation.AssessmentViewModel
import org.sagebionetworks.assessmentmodel.presentation.ui.theme.*
import org.sagebionetworks.motorcontrol.R
import org.sagebionetworks.motorcontrol.presentation.theme.countdownBeginText

@Composable
internal fun CountdownStepUi(
    assessmentViewModel: AssessmentViewModel?,
    duration: Double,
    countdown: MutableState<Long>,
    timer: StepTimer?
) {
    Column(modifier = Modifier.background(BackgroundGray)) {
        MotorControlPauseUi(
            assessmentViewModel = assessmentViewModel,
            onPause = { timer?.clear() },
            onUnpause = {
                countdown.value = (duration * 1000).toLong() // Resets countdown to initial value
                timer?.startTimer()
            }
        )
        Column(
            modifier = Modifier
                .fillMaxHeight()
                .fillMaxWidth()
                .padding(32.dp)
        ) {
            Spacer(modifier = Modifier.weight(1f))
            Text(
                text = stringResource(id = R.string.begin),
                textAlign = TextAlign.Center,
                style = countdownBeginText,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            )
            CountdownDial(countdownDuration = duration, countdown = countdown)
            Spacer(modifier = Modifier.weight(1f))
        }
    }
}

@Preview
@Composable
private fun InstructionStepPreview() {
    SageSurveyTheme {
        CountdownStepUi(
            assessmentViewModel = null,
            duration = 5.0,
            countdown = mutableStateOf(5),
            timer = null
        )
    }
}
