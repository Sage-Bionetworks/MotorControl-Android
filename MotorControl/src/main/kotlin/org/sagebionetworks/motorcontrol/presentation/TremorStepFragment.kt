//
//  TremorStepFragment.kt
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

package org.sagebionetworks.motorcontrol.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.material.MaterialTheme
import org.sagebionetworks.assessmentmodel.presentation.StepFragment
import org.sagebionetworks.assessmentmodel.presentation.databinding.ComposeQuestionStepFragmentBinding
import org.sagebionetworks.assessmentmodel.presentation.ui.theme.SageSurveyTheme
import org.sagebionetworks.assessmentmodel.serialization.loadDrawable
import org.sagebionetworks.motorcontrol.navigation.hand
import org.sagebionetworks.motorcontrol.presentation.compose.TremorStepUi
import org.sagebionetworks.motorcontrol.serialization.TremorStepObject
import org.sagebionetworks.motorcontrol.utils.MotorControlVibrator
import org.sagebionetworks.motorcontrol.utils.SpokenInstructionsConverter
import org.sagebionetworks.motorcontrol.state.TremorState

open class TremorStepFragment: StepFragment() {

    private var _binding: ComposeQuestionStepFragmentBinding? = null
    // This property is only valid between onCreateView and onDestroyView.
    private val binding get() = _binding!!

    private lateinit var step: TremorStepObject

    private lateinit var tremorState: TremorState

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        step = nodeState.node as TremorStepObject
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        _binding = ComposeQuestionStepFragmentBinding.inflate(layoutInflater, container, false)

        val drawable = step.imageInfo?.loadDrawable(requireContext())
        val tint = step.imageInfo?.tint ?: false

        binding.questionContent.setContent {
            val hand = stepViewModel.nodeState.parent?.node?.hand()
            tremorState = TremorState(
                identifier = step.identifier,
                hand = hand,
                duration = step.duration,
                context = requireContext(),
                spokenInstructions = SpokenInstructionsConverter.convertSpokenInstructions(
                    step.spokenInstructions,
                    step.duration.toInt(),
                    hand?.name ?: ""
                ),
                restartsOnPause = true,
                goForward = assessmentViewModel::goForward,
                vibrator = MotorControlVibrator(requireContext()),
                inputResult = stepViewModel.nodeState.parent?.currentResult?.inputResults,
                title = step.title ?: ""
            )
            tremorState.start()

            SageSurveyTheme {
                TremorStepUi(
                    assessmentViewModel = assessmentViewModel,
                    tremorState = tremorState,
                    image = drawable,
                    imageTintColor = if (tint) {
                        MaterialTheme.colors.primary
                    } else {
                        null
                    }
                )
            }
        }
        return binding.root
    }

    override fun onDestroyView() {
        tremorState.cancel()
        super.onDestroyView()
        _binding = null
    }
}