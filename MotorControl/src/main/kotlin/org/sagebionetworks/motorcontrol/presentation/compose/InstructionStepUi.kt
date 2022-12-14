//
//  InstructionStepUi.kt
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

import android.graphics.drawable.Drawable
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.sagebionetworks.assessmentmodel.presentation.AssessmentViewModel
import org.sagebionetworks.assessmentmodel.presentation.R
import org.sagebionetworks.assessmentmodel.presentation.compose.BottomNavigation
import org.sagebionetworks.assessmentmodel.presentation.ui.theme.*
import org.sagebionetworks.motorcontrol.presentation.theme.ImageBackgroundColor

@Composable
internal fun InstructionStepUi(
    modifier: Modifier = Modifier,
    assessmentViewModel: AssessmentViewModel?,
    image: Drawable?,
    animations: ArrayList<Drawable>,
    animationIndex: MutableState<Int>,
    flippedImage: Boolean,
    imageTintColor: Color?,
    title: String?,
    detail: String?,
    nextButtonText: String) {
    val imageModifier = if (flippedImage) {
        Modifier
            .fillMaxSize()
            .scale(-1F, 1F)
    } else {
        Modifier.fillMaxSize()
    }
    Box {
        Column(modifier = Modifier.background(BackgroundGray)) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .weight(1f, false)
            ) {
                if (image != null) {
                    SingleImageUi(
                        image = image,
                        surveyTint = ImageBackgroundColor,
                        imageModifier = imageModifier,
                        imageTintColor = imageTintColor
                    )
                }
                if (animations.isNotEmpty()) {
                    AnimationImageUi(
                        animations = animations,
                        surveyTint = ImageBackgroundColor,
                        currentImage = animationIndex,
                        imageTintColor = imageTintColor,
                        imageModifier = imageModifier
                    )
                }
                StepBodyTextUi(title, detail, modifier)
            }
            Box(modifier = Modifier.padding(horizontal = 20.dp)) {
                val backEnabled = assessmentViewModel?.assessmentNodeState?.allowBackNavigation() == true
                BottomNavigation(
                    onBackClicked = { assessmentViewModel?.goBackward() },
                    onNextClicked = { assessmentViewModel?.goForward() },
                    nextText = nextButtonText,
                    backEnabled = backEnabled,
                    backVisible = backEnabled
                )
            }
        }
        MotorControlPauseUi(assessmentViewModel = assessmentViewModel)
    }
}


@Preview
@Composable
private fun InstructionStepPreview() {
    SageSurveyTheme {
        InstructionStepUi(
            assessmentViewModel = null,
            image = null,
            animations = ArrayList(),
            animationIndex = mutableStateOf(0),
            flippedImage = false,
            imageTintColor = null,
            title = "Title",
            detail = "Details",
            nextButtonText = stringResource(R.string.start)
        )
    }
}
